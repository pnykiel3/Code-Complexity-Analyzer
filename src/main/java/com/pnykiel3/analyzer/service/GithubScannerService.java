package com.pnykiel3.analyzer.service;

import com.pnykiel3.analyzer.dto.DirectoryAnalysisResult;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GithubScannerService {

    private final DirectoryScannerService directoryScannerService;

    public GithubScannerService(DirectoryScannerService directoryScannerService) {
        this.directoryScannerService = directoryScannerService;
    }

    public DirectoryAnalysisResult scanRepository(String repoUrl) throws Exception {
        Path tempDir = Files.createTempDirectory("temporaryGithub-");

        try {

            System.out.println("Downloading Github repository " + repoUrl);
            try (Git git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(tempDir.toFile())
                    .call()) {
                System.out.println("Github repository cloned successfully");
            }
            return directoryScannerService.scanDirectory(tempDir.toString());
        } finally {
            FileSystemUtils.deleteRecursively(tempDir);
            System.out.println("Temporary files deleted successfully: " + tempDir);
        }
    }
}
