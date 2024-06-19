package com.study.solution.Service.Impl;

import com.study.common.DTO.TestCaseDto;
import com.study.common.Enum.Status;
import com.study.solution.DTO.Solution.SendTestSolutionRequest;
import com.study.solution.DTO.Solution.SolutionDto;
import com.study.solution.Entity.Solution;
import com.study.solution.Exceptions.Code.CodeCompilationException;
import com.study.solution.Exceptions.Code.CodeRuntimeException;
import com.study.solution.Exceptions.Code.TimeLimitException;
import com.study.solution.Exceptions.NotFound.SolutionNotFoundException;
import com.study.solution.Exceptions.NotFound.TaskNotFoundException;
import com.study.solution.Kafka.KafkaProducer;
import com.study.solution.Mapper.SolutionListMapper;
import com.study.solution.Mapper.SolutionMapper;
import com.study.solution.Repository.SolutionRepository;
import com.study.solution.Service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
                               JdbcTemplate jdbcTemplate) {
        this.webClient = webClient;
        this.solutionRepository = solutionRepository;
        this.solutionListMapper = solutionListMapper;
        this.solutionMapper = solutionMapper;
        this.kafkaProducer = kafkaProducer;
        this.testExecutorService = testExecutorService;
        this.jdbcTemplate = jdbcTemplate;
        this.maliciousWords = new HashSet<>();

        maliciousWords.add("shutdown");
        maliciousWords.add("docker");
        maliciousWords.add("docker.sock");
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
        maliciousWords.add("netsh");
        maliciousWords.add("powershell");
    }

    @Transactional
    public SolutionDto testSolution(Jwt user, UUID taskId, SendTestSolutionRequest request) throws IOException {
        List<TestCaseDto> tests = getTestCases(taskId).block();

        if (tests == null || tests.isEmpty()) {
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
        } else {
            CompletableFuture.runAsync(() -> {
                try {
                    testExecutorService.runCode(tests, code, timeLimit, solution, user);
                } catch (TimeLimitException e) {
                    log.info("Тайм лимит");
                    solution.setStatus(Status.TIME_LIMIT);
                } catch (CodeRuntimeException | IOException e) {
                    log.info("Ошибка в рантайме кода: " + e.getMessage());
                    solution.setStatus(Status.RUNTIME_ERROR);
                } catch (CodeCompilationException e) {
                    log.info("Код не был скомпилирован");
                    log.error(e.getMessage());
                    solution.setTestIndex(1L);
                    solution.setStatus(Status.COMPILATION_ERROR);
                }

                if (solution.getStatus() == Status.OK) {
                    long count = solutionRepository.countByUsernameAndTaskIdAndStatus(solution.getUsername(), solution.getTaskId(), Status.OK);
                    if (count == 0) {
                        kafkaProducer.sendMessage(solution.getTaskId(), solution.getUsername());
                    }
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
    public List<SolutionDto> getSolutions(Jwt user, UUID taskId) {
        String username = user.getClaim(USERNAME_CLAIM);

        return solutionListMapper.toModelList(solutionRepository.findAllByUsernameAndTaskId(username, taskId));
    }

    @Override
    public List<SolutionDto> getUserSolutions(UUID taskId, String username) {
        return solutionListMapper.toModelList(solutionRepository.findAllByUsernameAndTaskId(username, taskId));
    }

    @Override
    public SolutionDto getSolution(Jwt user, UUID solutionId) {
        Solution solution = solutionRepository.findSolutionById(solutionId)
                .orElseThrow(() -> new SolutionNotFoundException(solutionId));

        return solutionMapper.toDTO(solution);
    }

    private Mono<List<TestCaseDto>> getTestCases(UUID taskId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/learning/tests/" + taskId)
                        .build())
                .header("X-API-KEY", apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RuntimeException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Server error: " + response.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<List<TestCaseDto>>() {
                })
                .doOnError(e -> log.error("Error retrieving test cases", e));
    }
}
