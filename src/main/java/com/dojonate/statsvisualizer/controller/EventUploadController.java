package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Game;
import com.dojonate.statsvisualizer.service.GameService;
import com.dojonate.statsvisualizer.service.PlayerEventService;
import com.dojonate.statsvisualizer.util.EventFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class EventUploadController {

    private final Logger logger = LoggerFactory.getLogger(EventUploadController.class);
    @Autowired
    private EventFileParser eventFileParser;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerEventService playerEventService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadEventFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // Parse the content
            Game game = eventFileParser.parse(content.toString());

            // Save the game and related events
            gameService.save(game);
            playerEventService.saveAll(game.getPlayerEvents());

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to upload event file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the file.");
        }
    }
}
