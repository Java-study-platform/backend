package com.study.solution.Service.Impl;

import com.study.solution.Service.SolutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class SolutionServiceImpl implements SolutionService {

    public ResponseEntity<?> testSolution(String code, String username) throws IOException {
        // Здесь получим список тестов и просто по каждому тесту пробежим и сверим expectedOutput
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
        String input = "1\n2\n";
        // Ожидаемый вывод
        String expectedOutput = "3\n";

        String result = runCode(code, input);
        if (result.equals(expectedOutput)){
            System.out.println("победа");
        }
        else{
            System.out.println("поражение");
        }
        return ResponseEntity.ok().build();
    }

    private static String runCode(String code, String input) throws IOException {
        StringBuilder result = new StringBuilder();

        File tempFile = new File("Main.java");
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
}
