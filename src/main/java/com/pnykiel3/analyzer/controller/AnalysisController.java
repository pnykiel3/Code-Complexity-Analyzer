package com.pnykiel3.analyzer.controller;

import com.pnykiel3.analyzer.dto.DirectoryAnalysisResult;
import com.pnykiel3.analyzer.dto.FileMetrics;
import com.pnykiel3.analyzer.service.AnalysisService;
import com.pnykiel3.analyzer.service.DirectoryScannerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnalysisController {


    private final AnalysisService service;
    private final DirectoryScannerService directoryScannerService;

    public AnalysisController(AnalysisService service, DirectoryScannerService directoryScannerService) {
        this.service = service;
        this.directoryScannerService = directoryScannerService;
    }

    @PostMapping("/analyze")
    public FileMetrics analyze(@RequestParam("file") MultipartFile file) throws Exception {
        String code = new String (file.getBytes());
        String filename = file.getOriginalFilename();
        return service.analyze(filename, code);
    }

    @GetMapping("/analyze-directory")
    public DirectoryAnalysisResult analyzeDirectory(@RequestParam("path") String path) {
        try {
            return directoryScannerService.scanDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException("Scanning folder error: " + e.getMessage());
        }
    }

}
