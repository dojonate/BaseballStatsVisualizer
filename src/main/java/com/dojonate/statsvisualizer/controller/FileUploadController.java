package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.service.PlayerService;
import com.dojonate.statsvisualizer.util.RosFileParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/maint")
public class FileUploadController {

    private final PlayerService playerService;
    private final RosFileParser rosFileParser;

    @Value("${file.upload.temp-dir}")
    private String tempDir;

    public FileUploadController(PlayerService playerService, RosFileParser rosFileParser) {
        this.playerService = playerService;
        this.rosFileParser = rosFileParser;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadRosFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        try {
            // Ensure the temporary directory exists
            File tempDirectory = new File(tempDir);
            if (!tempDirectory.exists() && !tempDirectory.mkdirs()) {
                throw new IOException("Failed to create temporary directory: " + tempDirectory.getAbsolutePath());
            }
            else {
                System.out.println("Created temporary directory: " + tempDirectory.getAbsolutePath());
            }

            // Save the file temporarily
            File tempFile = new File(tempDirectory, file.getOriginalFilename());
            file.transferTo(tempFile);

            // Debug: Log file path
            System.out.println("Uploaded file: " + tempFile.getAbsolutePath() + ", size: " + tempFile.length());

            // Parse and save players
            List<Player> players = rosFileParser.parseRosFile(tempFile.toPath());
            playerService.saveAll(players);

            // Clean up the temporary file
            if (!tempFile.delete()) {
                System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
            }

            return ResponseEntity.ok("File uploaded and processed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file.");
        }
    }
}
