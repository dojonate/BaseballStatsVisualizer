package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.service.RosterEntryService;
import com.dojonate.statsvisualizer.service.TeamService;
import com.dojonate.statsvisualizer.util.RosFileParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/rosters")
public class RosterUploadController {

    private static final int TEAM_ABBREVIATION_LENGTH = 3;

    private final RosterEntryService rosterEntryService;
    private final TeamService teamService;
    private final RosFileParser rosFileParser;

    @Value("${file.upload.temp-dir}")
    private String tempDir;

    public RosterUploadController(RosterEntryService rosterEntryService, TeamService teamService, RosFileParser rosFileParser) {
        this.rosterEntryService = rosterEntryService;
        this.teamService = teamService;
        this.rosFileParser = rosFileParser;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadRosFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        try {
            File uploadedFile = saveFileTemporarily(file);
            String teamAbbreviation = extractTeamAbbreviation(uploadedFile.getName());
            Team team = teamService.findOrCreateTeam(teamAbbreviation, "Unknown Team", "Unknown League");
            processRosterFile(uploadedFile, team);
            cleanupTemporaryFile(uploadedFile);
            return ResponseEntity.ok("File uploaded and processed successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file: " + e.getMessage());
        }
    }

    private File saveFileTemporarily(MultipartFile file) throws IOException {
        File tempDirectory = new File(tempDir);
        if (!tempDirectory.exists() && !tempDirectory.mkdirs()) {
            throw new IOException("Failed to create temporary directory: " + tempDirectory.getAbsolutePath());
        }
        File uploadedFile = new File(tempDirectory, file.getOriginalFilename());
        file.transferTo(uploadedFile);
        return uploadedFile;
    }

    private String extractTeamAbbreviation(String fileName) {
        return fileName.substring(0, TEAM_ABBREVIATION_LENGTH);
    }

    private void processRosterFile(File uploadedFile, Team team) throws IOException {
        List<RosterEntry> rosterEntries = rosFileParser.parseRosFile(uploadedFile.toPath(), team);
        rosterEntryService.saveAll(rosterEntries);
    }

    private void cleanupTemporaryFile(File uploadedFile) {
        if (!uploadedFile.delete()) {
            System.err.println("Failed to delete temporary file: " + uploadedFile.getAbsolutePath());
        }
    }
}
