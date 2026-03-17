package com.pnykiel3.analyzer.parser;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class PythonParser implements FunctionParser {

    @Override
    public boolean supports(String extension) {
        return extension.equals("py");
    }

    @Override
    public int countFunctions(String code) {

        // Python script
        String pythonAstScript =
                "import ast\n" +
                        "import sys\n" +
                        "try:\n" +
                        "    source = sys.stdin.read()\n" +
                        "    tree = ast.parse(source)\n" +
                        "    count = sum(1 for node in ast.walk(tree) if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef)))\n" +
                        "    print(count)\n" +
                        "except Exception:\n" +
                        "    print(0)\n";

        try {

            ProcessBuilder processBuilder = new ProcessBuilder("python3", "-c", pythonAstScript);
            Process process = processBuilder.start();

            try (OutputStream os = process.getOutputStream()) {
                os.write(code.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String output = reader.readLine();
                process.waitFor();

                if (output != null) {
                    return Integer.parseInt(output.trim());
                }
            }

        } catch (Exception e) {
            System.err.println("Python parser error: " + e.getMessage());
        }

        return 0;
    }
}