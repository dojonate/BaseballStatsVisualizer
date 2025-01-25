package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.service.TeamService;
import com.dojonate.statsvisualizer.util.CsvTeamParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class TeamUploadController {

    private static final Logger logger = LoggerFactory.getLogger(TeamUploadController.class);

    private final TeamService teamService;
    private final CsvTeamParser csvTeamParser;

    @Value("${file.upload.temp-dir}")
    private String tempDir;

    private static final String FILE_EMPTY_ERROR = "The uploaded file is empty.";
    private static final String PROCESSING_ERROR = "Failed to process the file.";
    private static final String SUCCESS_RESPONSE = "Teams uploaded successfully.";
    private static final int INTERNAL_SERVER_ERROR_STATUS = 500;

    public TeamUploadController(TeamService teamService, CsvTeamParser csvTeamParser) {
        this.teamService = teamService;
        this.csvTeamParser = csvTeamParser;
    }

    @PostMapping("/teams")
    public ResponseEntity<String> handleCsvUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            logger.warn("File upload failed: file is empty.");
            return ResponseEntity.badRequest().body(FILE_EMPTY_ERROR);
        }

        File temporaryFile = null;

        try {
            temporaryFile = createTemporaryFile(file);
            return handleFileProcessing(temporaryFile);
        } catch (IOException e) {
            logger.error("Error processing file upload: {}", e.getMessage(), e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR_STATUS).body(PROCESSING_ERROR);
        } finally {
            cleanUpTemporaryFile(temporaryFile);
        }
    }

    private ResponseEntity<String> handleFileProcessing(File file) throws IOException {
        List<Team> teams = csvTeamParser.parseTeamCsv(file.toPath());
        teamService.saveAll(teams);
        logger.info("Successfully imported {} teams.", teams.size());
        return ResponseEntity.ok("Successfully imported " + teams.size() + " teams.");
    }

    private File createTemporaryFile(MultipartFile file) throws IOException {
        File tempFile = new File(tempDir, file.getOriginalFilename());
        file.transferTo(tempFile);
        logger.info("Temporary file created at {}", tempFile.getAbsolutePath());
        return tempFile;
    }

    private void cleanUpTemporaryFile(File file) {
        if (file != null) {
            if (!tryDeleteFile(file)) {
                logger.warn("Failed to delete temporary file: {}", file.getAbsolutePath());
            } else {
                logger.info("Temporary file deleted successfully: {}", file.getAbsolutePath());
            }
        }
    }

    private boolean tryDeleteFile(File file) {
        return file.delete();
    }
}