package com.pnykiel3.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
    private int loc; // lines of code
    private int functionCount; // number of declared functions
    private int cyclomaticComplexity;

}
