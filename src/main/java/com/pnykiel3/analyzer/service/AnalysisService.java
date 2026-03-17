package com.pnykiel3.analyzer.service;

import com.pnykiel3.analyzer.dto.FileMetrics;
import com.pnykiel3.analyzer.parser.FunctionParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {

    private final List<FunctionParser> parsers;

    public AnalysisService(List<FunctionParser> parsers) {
        this.parsers = parsers;
    }

    private String getFileExtension (String filename) {
        if ( filename == null || !filename.contains(".") )  return "";
        return filename.substring(filename.indexOf('.')+1).toLowerCase();
    }


    public FileMetrics analyze(String filename, String code) throws Exception {
        if (code == null || code.trim().isEmpty()) return new FileMetrics(0,0,1);

        String extension = getFileExtension(filename);

        FunctionParser activeParser = parsers.stream()
                .filter(parser -> parser.supports(extension))
                .findFirst().orElse(null);


        if (activeParser != null) {
            return activeParser.analyzeMetrics(code);
        } else throw new Exception("Uploaded the file is not supported yet");
    }
}
