package com.study.solution.Service.Impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Frame;
import com.study.common.DTO.TestCaseDto;
import com.study.common.Enum.Status;
import com.study.solution.DTO.Test.TestDto;
import com.study.solution.Entity.Solution;
import com.study.solution.Entity.Test;
import com.study.solution.Exceptions.Code.CodeCompilationException;
import com.study.solution.Exceptions.Code.CodeRuntimeException;
import com.study.solution.Exceptions.Code.TimeLimitException;
import com.study.solution.Mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.study.common.Constants.Consts.USERNAME_CLAIM;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestExecutorService {
    private final DockerClient dockerClient;
    private final TestMapper testMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final JdbcTemplate jdbcTemplate;

    private static final String DOCKER_IMAGE = "openjdk:17-oracle";

    protected void runCode(List<TestCaseDto> tests, String code, long timeLimit, Solution solution, Jwt user) throws IOException, CodeCompilationException, CodeRuntimeException, TimeLimitException {
        Path path = Files.createTempDirectory("compile");
        File tempFile = new File(path.toAbsolutePath() + "/Main.java");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(code);
        }

        Process compileProcess = Runtime.getRuntime().exec("javac " + tempFile.getAbsolutePath());
        try {
            boolean compileResult = compileProcess.waitFor(5000, TimeUnit.MILLISECONDS);
            log.info("compileResult равен: " + compileResult);
            if (!compileResult) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));

                StringBuilder errorBuilder = new StringBuilder();
                String line;

                while ((line = errorReader.readLine()) != null) {
                    errorBuilder.append(line).append("\n");
                }
                errorReader.close();

                log.error("Ошибка во время компиляции: " + errorBuilder.toString());

                saveTestOnCompilationError(tests, solution, errorBuilder, Status.TIME_LIMIT);

                throw new CodeCompilationException(errorBuilder.toString());
            }

            BufferedReader stdError = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder errorBuilder = new StringBuilder();
            String line;
            while ((line = stdError.readLine()) != null) {
                errorBuilder.append(line).append("\n");
            }
            stdError.close();

            if (!errorBuilder.isEmpty()) {
                log.error("Ошибка во время компиляции: " + errorBuilder.toString());
                saveTestOnCompilationError(tests, solution, errorBuilder, Status.COMPILATION_ERROR);
                log.error("Выкидываю ошибку");
                throw new CodeCompilationException(errorBuilder.toString());
            }
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода во время компиляции", e);
            saveTestOnCompilationError(tests, solution, null, Status.COMPILATION_ERROR);
            throw new CodeCompilationException("Ошибка ввода-вывода во время компиляции");
        } catch (InterruptedException e) {
            saveTestOnCompilationError(tests, solution, null, Status.COMPILATION_ERROR);

            throw new CodeCompilationException(e.getMessage());
        } finally {
            compileProcess.destroy();
        }

        CreateContainerResponse container = dockerClient.createContainerCmd(DOCKER_IMAGE)
                .withWorkingDir("/code")
                .exec();

        String containerId = container.getId();
        String classFilePath = tempFile.getParent() + "/Main.class";
        dockerClient.copyArchiveToContainerCmd(containerId)
                .withRemotePath("/code")
                .withHostResource(classFilePath)
                .exec();

        dockerClient.startContainerCmd(containerId).exec();

        for (TestCaseDto test : tests) {
            StringBuilder result = new StringBuilder();
            StringBuilder errorResult = new StringBuilder();

            String input = test.getExpectedInput();
            Long testIndex = test.getIndex();
            log.info("Тест номер: " + testIndex);

            Test testEntity = new Test();
            testEntity.setId(UUID.randomUUID());
            testEntity.setSolution(solution);
            testEntity.setTestInput(input);
            testEntity.setStatus(Status.PENDING);
            testEntity.setTestIndex(testIndex);
            solution.setTestIndex(testIndex);

            sendWebSocketMessage(testMapper.toDTO(testEntity), solution.getId());

            ExecCreateCmdResponse runCmd = dockerClient.execCreateCmd(containerId)
                    .withCmd("sh", "-c", "echo \"" + input + "\" | java -cp . Main")
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();

            try {
                boolean timeFlag = dockerClient.execStartCmd(runCmd.getId())
                        .exec(new ResultCallback.Adapter<Frame>() {
                            @Override
                            public void onNext(Frame item) {
                                String payload = new String(item.getPayload(), StandardCharsets.UTF_8);
                                String lowerCasePayload = payload.toLowerCase();
                                if (lowerCasePayload.contains("error")
                                        || lowerCasePayload.contains("caused by")
                                        || lowerCasePayload.contains("exception")) {
                                    log.warn("Записываю payload в ошибку: " + payload);
                                    errorResult.append(payload).append("\n");
                                } else {
                                    result.append(payload);
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                errorResult.append(throwable.getMessage()).append("\n");
                            }
                        }).awaitCompletion(timeLimit, TimeUnit.MILLISECONDS);

                log.info("timeFlag: {}", timeFlag);
                if (!timeFlag) {
                    log.info("Время выполнения превышено");
                    throw new TimeLimitException();
                }
            } catch (DockerException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            } catch (InterruptedException | TimeLimitException e) {
                stopContainer(containerId);
                testEntity.setTestOutput("Time limit");
                testEntity.setStatus(Status.TIME_LIMIT);
                log.info("Тесту поставлен тайм лимит");
                saveTest(testEntity, solution);
                sendWebSocketMessage(testMapper.toDTO(testEntity), solution.getId());
                throw new TimeLimitException();
            }

            String errorText = errorResult.toString();
            log.info(errorText);
            if (!errorText.equals("null") && !errorText.isEmpty()) {
                stopContainer(containerId);
                testEntity.setStatus(Status.RUNTIME_ERROR);
                saveTest(testEntity, solution);
                sendWebSocketMessage(testMapper.toDTO(testEntity), solution.getId());
                throw new CodeRuntimeException(errorResult.toString());
            }

            String strResult = result.toString()
                    .replaceAll("\n\n", "\n")
                    .replaceAll(" \n", "\n").trim();

            log.info(strResult);
            if (!strResult.isEmpty()) {
                log.info("Результат теста: " + strResult);
                testEntity.setTestOutput(strResult);

                if (strResult.equals(test.getExpectedOutput())) {
                    testEntity.setStatus(Status.OK);
                    if (test.getIndex() == tests.size()) {
                        solution.setStatus(Status.OK);
                        solution.setTestIndex(test.getIndex());
                    }
                } else {
                    testEntity.setStatus(Status.WRONG_ANSWER);
                    solution.setStatus(Status.WRONG_ANSWER);
                    solution.setTestIndex(test.getIndex());
                    saveTest(testEntity, solution);
                    break;
                }
            }

            log.info("Проверяю статус теста");
            if (testEntity.getStatus() == Status.PENDING) {
                log.info("Статус теста = PENDING");
                stopContainer(containerId);
                testEntity.setStatus(Status.TIME_LIMIT);
                testEntity.setTestOutput("Time limit");
                saveTest(testEntity, solution);
                sendWebSocketMessage(testMapper.toDTO(testEntity), solution.getId());
                throw new TimeLimitException();
            }

            saveTest(testEntity, solution);

            sendWebSocketMessage(testMapper.toDTO(testEntity), solution.getId());
        }

        stopContainer(containerId);

        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTestOnCompilationError(List<TestCaseDto> tests, Solution solution, StringBuilder errorBuilder, Status status) {
        TestCaseDto testCase = tests.get(0);

        Test testEntity = new Test();
        testEntity.setId(UUID.randomUUID());
        testEntity.setSolution(solution);
        testEntity.setTestInput(testCase.getExpectedInput());
        if (status == Status.TIME_LIMIT) {
            testEntity.setTestOutput("Time limit");
        } else if (errorBuilder == null || errorBuilder.isEmpty()) {
            testEntity.setTestOutput("Ошибка компиляции");
        } else {
            testEntity.setTestOutput(errorBuilder.toString());
        }

        testEntity.setStatus(status);
        testEntity.setTestIndex(testCase.getIndex());
        log.info("Попытка сохранить тест");

        saveTest(testEntity, solution);
    }

    private void saveTest(Test testEntity, Solution solution) {
        try {
            int rowsAffected = jdbcTemplate.update(
                    "INSERT INTO tests (id, test_index, test_input, test_output, test_time, status, solution_id) " +
                            "VALUES (?, ?, ?, ?, current_timestamp, ?, ?)",
                    testEntity.getId(), testEntity.getTestIndex(), testEntity.getTestInput(),
                    testEntity.getTestOutput(), testEntity.getStatus().toString(), solution.getId()
            );
            log.info("Inserted " + rowsAffected + " row(s).");
        } catch (DataAccessException e) {
            log.error("Failed to save test on compilation error: " + e.getMessage());
            throw new RuntimeException("Failed to save test on compilation error", e);
        }
    }

    private void sendWebSocketMessage(TestDto testDto, UUID solutionId) {
        try {
            log.info("Послал сообщений с тестом: " + testDto.getId() + " " + testDto.getStatus());
            messagingTemplate.convertAndSend(String.format("/solution/%s/test", solutionId.toString()), testDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopContainer(String containerId){
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerId).exec();
        if (inspectContainerResponse != null && inspectContainerResponse.getState().getRunning()) {
            dockerClient.stopContainerCmd(containerId).exec();
        }

        dockerClient.removeContainerCmd(containerId).exec();
    }
}
