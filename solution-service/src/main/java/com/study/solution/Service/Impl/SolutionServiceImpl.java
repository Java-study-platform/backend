package com.study.solution.Service.Impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.study.common.DTO.TestCaseDto;
import com.study.common.Enum.Status;
import com.study.common.Exceptions.ForbiddenException;
import com.study.solution.DTO.Solution.SendTestSolutionRequest;
import com.study.solution.DTO.Solution.SolutionDto;
import com.study.solution.DTO.Test.TestDto;
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
import com.study.solution.Mapper.TestMapper;
import com.study.solution.Repository.SolutionRepository;
import com.study.solution.Repository.TestRepository;
import com.study.solution.Service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
    private final SolutionListMapper solutionListMapper;
    private final SolutionMapper solutionMapper;
    private final HashSet<String> maliciousWords;
    private final KafkaProducer kafkaProducer;
    private final TestExecutorService testExecutorService;
    private final JdbcTemplate jdbcTemplate;


    @Value("${services.api-key}")
    private String apiKey;

    @Autowired
    public SolutionServiceImpl(WebClient webClient,
                               SolutionRepository solutionRepository,
                               SolutionListMapper solutionListMapper,
                               SolutionMapper solutionMapper,
                               KafkaProducer kafkaProducer,
                               TestExecutorService testExecutorService,
                               JdbcTemplate jdbcTemplate){
        this.webClient = webClient;
        this.solutionRepository = solutionRepository;
        this.solutionListMapper = solutionListMapper;
        this.solutionMapper = solutionMapper;
        this.kafkaProducer = kafkaProducer;
        this.testExecutorService = testExecutorService;
        this.jdbcTemplate = jdbcTemplate;
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
    public SolutionDto testSolution(Jwt user, UUID taskId, SendTestSolutionRequest request) throws IOException {
        List<TestCaseDto> tests = getTestCases(taskId).block();

        if (tests == null || tests.isEmpty()){
            throw new TaskNotFoundException(taskId);
        }

        Solution solution = new Solution();
        String code = request.getCode().replaceAll("(?m)^package\\s+.*?;", "").trim();

        solution.setSolutionCode(code);
        solution.setId(UUID.randomUUID());
        solution.setStatus(Status.PENDING);
        solution.setTaskId(taskId);
        solution.setTestIndex(0L);
        solution.setUsername(user.getClaim(USERNAME_CLAIM));

        jdbcTemplate.update(
                "INSERT INTO solutions (id, create_time, solution_code, status, task_id, test_index, username) " +
                        "VALUES (?, current_timestamp, ?, ?, ?, ?, ?)",
                solution.getId(), solution.getSolutionCode(), solution.getStatus().toString(),
                solution.getTaskId(), solution.getTestIndex(), solution.getUsername()
        );

        long timeLimit = tests.get(0).getTimeLimit();

        if (containsMaliciousWords(code, maliciousWords)) {
            solution.setStatus(Status.MALICIOUS_CODE);
            solutionRepository.save(solution);
        }
        else {
            CompletableFuture.runAsync(() -> {
                try {
                    testExecutorService.runCode(tests, code, timeLimit, solution, user);
                } catch (TimeLimitException e) {
                    log.info("Тайм лимит");
                    solution.setStatus(Status.TIME_LIMIT);
                } catch (CodeRuntimeException | IOException e) {
                    log.info("Ошибка в рантайме кода: " + e.getMessage());
                    e.printStackTrace();
                    solution.setStatus(Status.RUNTIME_ERROR);
                } catch (CodeCompilationException e) {
                    log.info("Код не был скомпилирован");
                    solution.setStatus(Status.COMPILATION_ERROR);
                }

                solutionRepository.save(solution);

//            kafkaProducer.sendMessage(user.getClaim(EMAIL_CLAIM),
//                    "Решение завершило проверку",
//                    String.format("Статус решения: %s", solution.getStatus()),
//                    true);
            });
        }

        return solutionMapper.toDTO(solution);
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

//    private static String runCode(String code, String input, long timeLimit) throws IOException {
//        StringBuilder result = new StringBuilder();
//
//        Path path = Files.createTempDirectory("compile");
//
//        File tempFile = new File(path.toAbsolutePath() + "\\Main.java");
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));;
//        writer.write(code);
//        writer.close();
//
//        Process compileProcess = Runtime.getRuntime().exec("javac " + tempFile.getAbsolutePath());
//        try {
//            int compileResult = compileProcess.waitFor();
//            if (compileResult != 0 ){
//                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
//
//                StringBuilder errorBuilder = new StringBuilder();
//                String line;
//
//                while ((line = errorReader.readLine()) != null) {
//                    errorBuilder.append(line).append("\n");
//                }
//                errorReader.close();
//
//                throw new CodeCompilationException(errorBuilder.toString());
//            }
//            else {
//                String command = String.format("java -cp %s Main", tempFile.getParent());
//
//                Process process = Runtime.getRuntime().exec(command);
//                try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
//                    inputWriter.write(input);
//                    inputWriter.flush();
//                }
//
//                ExecutorService executor = Executors.newSingleThreadExecutor();
//
//                Callable<String> outputTask = () -> {
//                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                        return reader.lines().collect(Collectors.joining("\n"));
//                    }
//                };
//
//                Callable<String> errorTask = () -> {
//                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
//                        return errorReader.lines().collect(Collectors.joining("\n"));
//                    }
//                };
//
//                try{
//                    Future<String> outputFuture = executor.submit(outputTask);
//                    Future<String> errorFuture = executor.submit(errorTask);
//
//    //                boolean finished = process.waitFor(timeLimit, TimeUnit.MILLISECONDS);
//    //                if (!finished) {
//    //                    process.destroy();
//    //                    executor.shutdownNow();
//    //                    throw new TimeLimitException();
//    //                }
//
//                    result.append(outputFuture.get(timeLimit, TimeUnit.MILLISECONDS)).append("\n");
//
//                    String errors = errorFuture.get();
//                    if (!errors.isEmpty()) {
//                        result.append(errors).append("\n");
//
//                        throw new CodeRuntimeException(result.toString());
//                    }
//
//                    if (process.exitValue() != 0) {
//                        throw new CodeRuntimeException(result.toString());
//                    }
//                }
//                catch (InterruptedException | ExecutionException e){
//                    throw new CodeRuntimeException(e.getMessage());
//                } catch (TimeoutException e) {
//                    process.destroy();
//                    executor.shutdownNow();
//
//                    throw new TimeLimitException();
//                }
//                finally {
//                    executor.shutdown();
//                }
//            }
//        }
//        catch (InterruptedException e){
//            throw new CodeRuntimeException(e.getMessage());
//        } finally {
//            File classFile = new File(tempFile.getParent(), "Main.class");
//
//            tempFile.delete();
//            classFile.delete();
//            Files.delete(path);
//        }
//
//        return result.toString();
//    }

    private Mono<List<TestCaseDto>> getTestCases(UUID taskId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/learning/tests/" + taskId)
                        .build())
                .header("X-API-KEY", apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RuntimeException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server error: " + response.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<List<TestCaseDto>>() {})
                .doOnError(e -> log.error("Error retrieving test cases", e));
    }
}
