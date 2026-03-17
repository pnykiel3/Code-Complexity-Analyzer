package com.pnykiel3.analyzer.parser;
import com.pnykiel3.analyzer.dto.FileMetrics;

public interface FunctionParser {
    // does the parser support the file
    boolean supports(String extension);

    // main logic
    FileMetrics analyzeMetrics(String code);

    default int countLines(String code) {
        String[] lines = code.replace("\r", "").split("\n");
        return lines.length;
    }
}