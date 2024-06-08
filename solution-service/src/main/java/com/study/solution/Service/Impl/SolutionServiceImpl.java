package com.study.solution.Service.Impl;

import com.study.common.DTO.TestCaseDto;
import com.study.solution.Service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {
    private final WebClient webClient;

    @Value("${services.api-key}")
    private String apiKey;

    public ResponseEntity<?> testSolution(UUID taskId, String code, String username) throws IOException {
        List<TestCaseDto> tests = getTestCases(taskId).block();

        code = "import java.io.BufferedReader;\n" +
                "import java.io.IOException;\n" +
                "import java.io.InputStreamReader;\n" +
                "\n" +
                "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) throws IOException {\n" +
                "        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));\n" +
                "        int a = Integer.parseInt(reader.readLine());\n" +
                "        int b = Integer.parseInt(reader.readLine());\n" +
                "\n" +
                "        System.out.println(a+b);\n" +
                "    }\n" +
                "}";

        if (tests != null && !tests.isEmpty()) {
            for (TestCaseDto test : tests) {
                String result = runCode(code, test.getExpectedInput());
                if (result.equals(test.getExpectedInput())) {
                    System.out.println("победа");
                } else {
                    System.out.println("поражение");
                }
            }
        }

        return ResponseEntity.ok().build();
    }

    private static String runCode(String code, String input) throws IOException {
        StringBuilder result = new StringBuilder();

        Path path = Files.createTempDirectory("compile");

        File tempFile = new File(path.toAbsolutePath() + "Main.java");
//File tempFile = File.createTempFile("Main", ".java");
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
                String command = String.format("java -cp %s %s", tempFile.getParent(), tempFile.getName());

                Process process = Runtime.getRuntime().exec(command);
                BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                inputWriter.write(input);
                inputWriter.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                reader.close();

                process.waitFor();
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        tempFile.delete();

        return result.toString();
    }

    private Mono<List<TestCaseDto>> getTestCases(UUID taskId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/learning/tests")
                        .queryParam("taskId", taskId)
                        .build())
                .header("X-API-KEY", apiKey)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TestCaseDto>>() {});
    }
}
