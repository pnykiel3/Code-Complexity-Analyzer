package com.pnykiel3.analyzer.parser;

import com.pnykiel3.analyzer.dto.FileMetrics;
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
    public FileMetrics analyzeMetrics(String code) {

        int linesCount = countLines(code);

        // Python script
        String pythonAstScript =
                "import ast\n" +
                        "import sys\n" +
                        "try:\n" +
                        "    source = sys.stdin.read()\n" +
                        "    tree = ast.parse(source)\n" +
                        "    funcs = sum(1 for node in ast.walk(tree) if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef)))\n" +
                        "    comp = 1 + sum(1 for node in ast.walk(tree) if isinstance(node, (ast.If, ast.For, ast.AsyncFor, ast.While, ast.ExceptHandler)))\n" +
                        "    print(f'{funcs},{comp}')\n" +
                        "except Exception:\n" +
                        "    print('0,1')\n";

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

                if (output != null && output.contains(",")) {
                    String[] parts = output.trim().split(",");
                    int functionCount = Integer.parseInt(parts[0]);
                    int complexity = Integer.parseInt(parts[1]);
                    return new FileMetrics(linesCount, functionCount, complexity);
                }
            }

        } catch (Exception e) {
            System.err.println("Python parser error: " + e.getMessage());
        }

        return new FileMetrics(0, 0, 1);
    }
}