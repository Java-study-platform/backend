package com.study.solution.Service.Impl;

import com.study.common.DTO.TestCaseDto;
import com.study.solution.DTO.SendTestSolutionRequest;
import com.study.solution.Entity.Solution;
import com.study.common.Enum.Status;
import com.study.solution.Repository.SolutionRepository;
import com.study.solution.Service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {
    private final WebClient webClient;
    private final SolutionRepository solutionRepository;

    @Value("${services.api-key}")
    private String apiKey;

    public String testSolution(Jwt user, UUID taskId, SendTestSolutionRequest request) throws IOException {
        List<TestCaseDto> tests = getTestCases(taskId).block();

        if (tests != null && !tests.isEmpty()) {
            Solution solution = new Solution();
            String code = request.getCode().replaceAll("(?m)^package\\s+.*?;", "").trim();
            solution.setSolutionCode(code);
            solution.setStatus(Status.PENDING);
            solution.setTaskId(taskId);
            solution.setTestIndex(0L);
            solution.setUsername(user.getClaim("preferred_username"));

            long timeLimit = tests.get(0).getTimeLimit();

            CompletableFuture.runAsync(() -> {
                String result;
                for (TestCaseDto test : tests) {
                    try {
                        result = runCode(code, test.getExpectedInput())
                                .replaceAll("\n\n", "\n")
                                .replaceAll(" \n", "\n").trim();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (result.equals(test.getExpectedOutput())) {
                        log.info("Победа");
                        solution.setStatus(Status.OK);
                        solution.setTestIndex(test.getIndex());
                    } else {
                        log.info("Поражение");
                        solution.setStatus(Status.WRONG_ANSWER);
                        solution.setTestIndex(test.getIndex());
                        break;
                    }
                }

                solutionRepository.save(solution);
            }).orTimeout(timeLimit, TimeUnit.MILLISECONDS);

            solutionRepository.save(solution);
        }

        return "Решение успешно отправлено";
    }

    private static String runCode(String code, String input) throws IOException {
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
                String line;

                while ((line = errorReader.readLine()) != null) {
                    log.error(line);
                }
                errorReader.close();
            }
            else {
                String command = String.format("java -cp %s Main", tempFile.getParent());

                Process process = Runtime.getRuntime().exec(command);
                try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    inputWriter.write(input);
                    inputWriter.flush();
                }

                Future<String> outputFuture = Executors.newSingleThreadExecutor().submit(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        return reader.lines().collect(Collectors.joining("\n"));
                    }
                });

                Future<String> errorFuture = Executors.newSingleThreadExecutor().submit(() -> {
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        return errorReader.lines().collect(Collectors.joining("\n"));
                    }
                });

                int runResult = process.waitFor();
                result.append(outputFuture.get()).append("\n");
                String errors = errorFuture.get();
                if (!errors.isEmpty()) {
                    log.error(errors);
                    result.append(errors).append("\n");
                }
                if (runResult != 0) {
                    return "Runtime Error:\n" + result.toString();
                }
            }
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
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
