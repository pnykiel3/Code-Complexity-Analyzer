package com.pnykiel3.analyzer.parser;

public interface FunctionParser {
    // does the parser support file
    boolean supports(String extension);

    // main logic
    int countFunctions(String code);
}