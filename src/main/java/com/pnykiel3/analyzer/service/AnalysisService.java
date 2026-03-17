package com.pnykiel3.analyzer.service;

import com.pnykiel3.analyzer.dto.AnalysisResult;
import com.pnykiel3.analyzer.parser.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {

    private List<FunctionParser> parsers;

    public AnalysisService(List<FunctionParser> parsers) {
        this.parsers = parsers;
    }

    private String getFileExtension (String filename) {
        if ( filename == null || !filename.contains(".") )  return "";
        return filename.substring(filename.indexOf('.')+1).toLowerCase();
    }
    public AnalysisResult analyze(String filename, String code){
        if (code == null || code.trim().isEmpty()) return new AnalysisResult(0,0,1);

        String[] lines = code.replace("\r", "").split("\n");
        int loc = lines.length;

        String extension = getFileExtension(filename);

        FunctionParser activeParser = parsers.stream()
                .filter(parser -> parser.supports(extension))
                .findFirst().orElse(null);

        int functionCount = 0;
        if (activeParser != null) {
            functionCount = activeParser.countFunctions(code);
        }
        return new AnalysisResult(loc, functionCount, 1);
    }
}
