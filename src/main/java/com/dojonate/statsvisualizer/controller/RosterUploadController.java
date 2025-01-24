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
            // Ensure the temporary directory exists
            File tempDirectory = new File(tempDir);
            if (!tempDirectory.exists() && !tempDirectory.mkdirs()) {
                throw new IOException("Failed to create temporary directory: " + tempDirectory.getAbsolutePath());
            }

            // Save the file temporarily
            File tempFile = new File(tempDirectory, file.getOriginalFilename());
            file.transferTo(tempFile);

            // Extract team abbreviation from filename (e.g., "2011TEX.ROS" -> "TEX")
            String fileName = tempFile.getName();
            String teamAbbreviation = fileName.substring(0, 3); // Assumes fixed format "2011TEX.ROS"

            // Find or create the team using TeamService
            Team team = teamService.findOrCreateTeam(teamAbbreviation, "Unknown Team", "Unknown League");

            // Parse the file and save roster entries
            List<RosterEntry> rosterEntries = rosFileParser.parseRosFile(tempFile.toPath(), team);
            rosterEntryService.saveAll(rosterEntries);

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
