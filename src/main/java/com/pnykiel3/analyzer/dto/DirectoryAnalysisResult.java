package com.pnykiel3.analyzer.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DirectoryAnalysisResult {
    private int totalLoc = 0;
    private int totalFunctions = 0;
    private int totalComplexity = 0;
    private final List<FileAnalysisDetail> files = new ArrayList<>();

    public void addFileResult(String filePath, FileMetrics fileMetrics) {
        this.files.add(new FileAnalysisDetail(filePath, fileMetrics));
        this.totalLoc += fileMetrics.getLinesCount();
        this.totalFunctions += fileMetrics.getFunctionCount();
        this.totalComplexity += fileMetrics.getCyclomaticComplexity();
    }
}
