package com.pnykiel3.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileAnalysisDetail {
    private final String filePath;
    private final FileMetrics fileMetrics;

}
