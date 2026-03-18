package com.pnykiel3.analyzer.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonPropertyOrder({ "totalLines", "totalFunctions", "totalComplexity", "files" })
public class DirectoryAnalysisResult {
    private int totalLines = 0;
    private int totalFunctions = 0;
    private int totalComplexity = 0;
    private final List<FileAnalysisDetail> files = new ArrayList<>();

    public void addFileResult(String filePath, FileMetrics fileMetrics) {
        this.files.add(new FileAnalysisDetail(filePath, fileMetrics));
        this.totalLines += fileMetrics.getLinesCount();
        this.totalFunctions += fileMetrics.getFunctionCount();
        this.totalComplexity += fileMetrics.getCyclomaticComplexity();
    }
}
