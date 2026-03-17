package com.pnykiel3.analyzer.controller;

import com.pnykiel3.analyzer.dto.AnalysisResult;
import com.pnykiel3.analyzer.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnalysisController {


    @Autowired
    private AnalysisService service;

    @PostMapping("/analyze")
    public AnalysisResult analyze(@RequestParam("file") MultipartFile file) throws Exception {
        String code = new String (file.getBytes());
        String filename = file.getOriginalFilename();
        return service.analyze(filename, code);
    }
}
