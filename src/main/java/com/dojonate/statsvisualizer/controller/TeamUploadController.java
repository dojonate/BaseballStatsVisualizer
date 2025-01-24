package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.service.TeamService;
import com.dojonate.statsvisualizer.util.CsvTeamParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamUploadController {

    private final TeamService teamService;
    private final CsvTeamParser csvTeamParser;

    @Value("${file.upload.temp-dir}")
    private String tempDir;

    public TeamUploadController(TeamService teamService, CsvTeamParser csvTeamParser) {
        this.teamService = teamService;
        this.csvTeamParser = csvTeamParser;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTeamCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        try {
            // Save the file temporarily
            File tempFile = new File(tempDir, file.getOriginalFilename());
            file.transferTo(tempFile);

            // Parse and save teams
            List<Team> teams = csvTeamParser.parseTeamCsv(tempFile.toPath());
            teamService.saveAll(teams);

            // Clean up temporary file
            if (!tempFile.delete()) {
                System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
            }

            return ResponseEntity.ok("Successfully imported " + teams.size() + " teams.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process file.");
        }
    }
}
