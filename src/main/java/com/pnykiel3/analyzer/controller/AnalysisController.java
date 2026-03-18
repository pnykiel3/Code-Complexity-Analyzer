package com.pnykiel3.analyzer.controller;

import com.pnykiel3.analyzer.dto.DirectoryAnalysisResult;
import com.pnykiel3.analyzer.dto.FileMetrics;
import com.pnykiel3.analyzer.service.AnalysisService;
import com.pnykiel3.analyzer.service.DirectoryScannerService;
import com.pnykiel3.analyzer.service.GithubScannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Code analyzer", description = "Static analysis API for code complexity")
public class AnalysisController {


    private final AnalysisService service;
    private final DirectoryScannerService directoryScannerService;
    private final GithubScannerService githubScannerService;

    public AnalysisController(AnalysisService service, DirectoryScannerService directoryScannerService, GithubScannerService githubScannerService) {
        this.service = service;
        this.directoryScannerService = directoryScannerService;
        this.githubScannerService = githubScannerService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze a file")
    public FileMetrics analyze(@Parameter(description = ".java or .py source file") @RequestParam("file") MultipartFile file) throws Exception {
        String code = new String (file.getBytes());
        String filename = file.getOriginalFilename();
        return service.analyze(filename, code);
    }

    @GetMapping("/analyze-directory")
    @Operation(summary = "Analyze a directory")
    public DirectoryAnalysisResult analyzeDirectory(@Parameter(description = "Absolute path") @RequestParam("path") String path) {
        try {
            return directoryScannerService.scanDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException("Scanning folder error: " + e.getMessage());
        }
    }

    @GetMapping("/analyze-repo")
    @Operation(summary = "Analyze a GitHub repository")
    public DirectoryAnalysisResult analyzeRepository(@Parameter(description = "Public repository URL") @RequestParam("url") String url) {
        try {
            return githubScannerService.scanRepository(url);
        } catch (Exception e) {
            throw new RuntimeException("Github folder error: " + e.getMessage());
        }
    }

}
