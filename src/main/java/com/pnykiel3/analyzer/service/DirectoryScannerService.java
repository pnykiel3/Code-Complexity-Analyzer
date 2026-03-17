package com.pnykiel3.analyzer.service;

import com.pnykiel3.analyzer.dto.DirectoryAnalysisResult;
import com.pnykiel3.analyzer.dto.FileMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DirectoryScannerService {

    private final AnalysisService analysisService;

    public DirectoryAnalysisResult scanDirectory(String directoryPath) throws IOException {
        Path startPath = Paths.get(directoryPath);

        if ( !Files.exists(startPath) || !Files.isDirectory(startPath) ) {
            throw new IllegalArgumentException("Path does not exist or is not a folder " + directoryPath);
        }

        DirectoryAnalysisResult finalResult = new DirectoryAnalysisResult();

        try (Stream<Path> paths = Files.walk(startPath)) {
            paths.filter(Files::isRegularFile) // ignore folders
                    .filter(this::isSupportedFile) // only .py and .java files
                    .forEach(path -> {
                        try {

                            String content = Files.readString(path);
                            String filename = path.getFileName().toString();

                            FileMetrics fileResult = analysisService.analyze(filename, content);

                            String relativePath = startPath.relativize(path).toString();
                            finalResult.addFileResult(relativePath, fileResult);

                        } catch (IOException e) {
                            System.err.println("Cannot read file: " + path);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return finalResult;
    }

    private boolean isSupportedFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".java") || name.endsWith(".py");
    }

}
