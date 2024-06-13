package com.study.solution.Service.Impl;

import com.study.common.DTO.TestCaseDto;
import com.study.common.Enum.Status;
import com.study.common.Exceptions.ForbiddenException;
import com.study.solution.DTO.Solution.SendTestSolutionRequest;
import com.study.solution.DTO.Solution.SolutionDto;
import com.study.solution.Entity.Solution;
import com.study.solution.Entity.Test;
import com.study.solution.Exceptions.Code.CodeCompilationException;
import com.study.solution.Exceptions.Code.CodeRuntimeException;
import com.study.solution.Exceptions.Code.TimeLimitException;
import com.study.solution.Exceptions.NotFound.SolutionNotFoundException;
import com.study.solution.Exceptions.NotFound.TaskNotFoundException;
import com.study.solution.Kafka.KafkaProducer;
import com.study.solution.Mapper.SolutionListMapper;
import com.study.solution.Mapper.SolutionMapper;
import com.study.solution.Repository.SolutionRepository;
import com.study.solution.Repository.TestRepository;
import com.study.solution.Service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.study.common.Constants.Consts.EMAIL_CLAIM;
import static com.study.common.Constants.Consts.USERNAME_CLAIM;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {
    private final WebClient webClient;
    private final SolutionRepository solutionRepository;
    private final TestRepository testRepository;
    private final SolutionListMapper solutionListMapper;
    private final SolutionMapper solutionMapper;
    private final HashSet<String> maliciousWords;
    private final KafkaProducer kafkaProducer;

    @Value("${services.api-key}")
    private String apiKey;

    @Autowired
    public SolutionServiceImpl(WebClient webClient,
                               SolutionRepository solutionRepository,
                               TestRepository testRepository,
                               SolutionListMapper solutionListMapper,
                               SolutionMapper solutionMapper,
                               KafkaProducer kafkaProducer){
        this.webClient = webClient;
        this.solutionRepository = solutionRepository;
        this.testRepository = testRepository;
        this.solutionListMapper = solutionListMapper;
        this.solutionMapper = solutionMapper;
        this.kafkaProducer = kafkaProducer;
        this.maliciousWords = new HashSet<>();

        maliciousWords.add("shutdown");
        maliciousWords.add("File");
        maliciousWords.add("exec");
        maliciousWords.add("Runtime.getRuntime");
        maliciousWords.add("System.exit");
        maliciousWords.add("ProcessBuilder");
        maliciousWords.add("delete");
        maliciousWords.add("rm");
        maliciousWords.add("format");
        maliciousWords.add("del");
        maliciousWords.add("mkfs");
        maliciousWords.add("dd");
        maliciousWords.add("netsh");
        maliciousWords.add("powershell");
    }

    @Transactional
    public String testSolution(Jwt user, UUID taskId, SendTestSolutionRequest request) throws IOException {
        List<TestCaseDto> tests = getTestCases(taskId).block();


        if (tests != null && !tests.isEmpty()) {
            Solution solution = new Solution();
            String code = request.getCode().replaceAll("(?m)^package\\s+.*?;", "").trim();

            solution.setSolutionCode(code);
            solution.setStatus(Status.PENDING);
            solution.setTaskId(taskId);
            solution.setTestIndex(0L);
            solution.setUsername(user.getClaim(USERNAME_CLAIM));
            solutionRepository.saveAndFlush(solution);

            long timeLimit = tests.get(0).getTimeLimit();

            if (containsMaliciousWords(code, maliciousWords)) {
                solution.setStatus(Status.MALICIOUS_CODE);
                solutionRepository.save(solution);
            }
            else {
                CompletableFuture.runAsync(() -> {
                    String result = null;
                    for (TestCaseDto test : tests) {
                        Long testIndex = test.getIndex();
                        String input = test.getExpectedInput();

                        Test testEntity = new Test();
                        testEntity.setSolution(solution);
                        testEntity.setTestInput(input);
                        testEntity.setStatus(Status.PENDING);
                        testEntity.setTestIndex(testIndex);
                        testRepository.save(testEntity);

                        try {
                            result = runCode(code, input, timeLimit)
                                    .replaceAll("\n\n", "\n")
                                    .replaceAll(" \n", "\n").trim();
                        } catch (TimeLimitException e) {
                            solution.setStatus(Status.TIME_LIMIT);
                            solution.setTestIndex(testIndex);
                            testEntity.setStatus(Status.TIME_LIMIT);
                        } catch (CodeRuntimeException e) {
                            solution.setStatus(Status.RUNTIME_ERROR);
                            solution.setTestIndex(testIndex);

                            testEntity.setStatus(Status.RUNTIME_ERROR);
                            testEntity.setTestOutput(e.getMessage());
                        } catch (CodeCompilationException e) {
                            solution.setStatus(Status.COMPILATION_ERROR);
                            solution.setTestIndex(testIndex);

                            testEntity.setStatus(Status.COMPILATION_ERROR);
                            testEntity.setTestOutput(e.getMessage().replaceAll("^[A-Za-z]:\\\\(?:[^\\\\\\n]+\\\\)*compile[^:\\n]+:", ""));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if (result != null) {
                            testEntity.setTestOutput(result);

                            if (result.equals(test.getExpectedOutput())) {
                                testEntity.setStatus(Status.OK);
                                if (test.getIndex() == tests.size()) {
                                    solution.setStatus(Status.OK);
                                    solution.setTestIndex(test.getIndex());
                                }
                            } else {
                                testEntity.setStatus(Status.WRONG_ANSWER);
                                solution.setStatus(Status.WRONG_ANSWER);
                                solution.setTestIndex(test.getIndex());
                                break;
                            }
                        }

                        testRepository.save(testEntity);
                    }

                    solutionRepository.save(solution);

                    kafkaProducer.sendMessage(user.getClaim(EMAIL_CLAIM),
                            "Решение завершило проверку",
                            String.format("Статус решения: %s", solution.getStatus()),
                            true);
                });
            }
        }
        else{
            throw new TaskNotFoundException(taskId);
        }

        return "Решение успешно отправлено";
    }

    private static boolean containsMaliciousWords(String code, Set<String> maliciousWords) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(code))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : maliciousWords) {
                    if (line.contains(word)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<SolutionDto> getUserSolutions(Jwt user, UUID taskId){
        String username = user.getClaim(USERNAME_CLAIM);

        return solutionListMapper.toModelList(solutionRepository.findAllByUsernameAndTaskId(username, taskId));
    }

    @Override
    public SolutionDto getSolution(Jwt user, UUID solutionId){
        String username = user.getClaim(USERNAME_CLAIM);
        Solution solution = solutionRepository.findSolutionById(solutionId)
                .orElseThrow(() -> new SolutionNotFoundException(solutionId));

        if (!username.equals(solution.getUsername())){
            throw new ForbiddenException();
        }

        return solutionMapper.toDTO(solution);
    }

    private static String runCode(String code, String input, long timeLimit) throws IOException {
        StringBuilder result = new StringBuilder();

        Path path = Files.createTempDirectory("compile");

        File tempFile = new File(path.toAbsolutePath() + "\\Main.java");

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));;
        writer.write(code);
        writer.close();

        Process compileProcess = Runtime.getRuntime().exec("javac " + tempFile.getAbsolutePath());
        try {
            int compileResult = compileProcess.waitFor();
            if (compileResult != 0 ){
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));

                StringBuilder errorBuilder = new StringBuilder();
                String line;

                while ((line = errorReader.readLine()) != null) {
                    errorBuilder.append(line).append("\n");
                }
                errorReader.close();

                throw new CodeCompilationException(errorBuilder.toString());
            }
            else {
                String command = String.format("java -cp %s Main", tempFile.getParent());

                Process process = Runtime.getRuntime().exec(command);
                try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    inputWriter.write(input);
                    inputWriter.flush();
                }

                ExecutorService executor = Executors.newSingleThreadExecutor();

                Callable<String> outputTask = () -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        return reader.lines().collect(Collectors.joining("\n"));
                    }
                };

                Callable<String> errorTask = () -> {
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        return errorReader.lines().collect(Collectors.joining("\n"));
                    }
                };

                try{
                    Future<String> outputFuture = executor.submit(outputTask);
                    Future<String> errorFuture = executor.submit(errorTask);

    //                boolean finished = process.waitFor(timeLimit, TimeUnit.MILLISECONDS);
    //                if (!finished) {
    //                    process.destroy();
    //                    executor.shutdownNow();
    //                    throw new TimeLimitException();
    //                }

                    result.append(outputFuture.get(timeLimit, TimeUnit.MILLISECONDS)).append("\n");

                    String errors = errorFuture.get();
                    if (!errors.isEmpty()) {
                        result.append(errors).append("\n");

                        throw new CodeRuntimeException(result.toString());
                    }

                    if (process.exitValue() != 0) {
                        throw new CodeRuntimeException(result.toString());
                    }
                }
                catch (InterruptedException | ExecutionException e){
                    throw new CodeRuntimeException(e.getMessage());
                } catch (TimeoutException e) {
                    process.destroy();
                    executor.shutdownNow();

                    throw new TimeLimitException();
                }
                finally {
                    executor.shutdown();
                }
            }
        }
        catch (InterruptedException e){
            throw new CodeRuntimeException(e.getMessage());
        } finally {
            File classFile = new File(tempFile.getParent(), "Main.class");

            tempFile.delete();
            classFile.delete();
            Files.delete(path);
        }


        return result.toString();
    }

    private Mono<List<TestCaseDto>> getTestCases(UUID taskId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/learning/tests/" + taskId)
                        .build())
                .header("X-API-KEY", apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return Mono.error(new RuntimeException("Client error: " + response.statusCode()));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    return Mono.error(new RuntimeException("Server error: " + response.statusCode()));
                })
                .bodyToMono(new ParameterizedTypeReference<List<TestCaseDto>>() {})
                .doOnError(e -> log.error("Error retrieving test cases", e));
    }
}
