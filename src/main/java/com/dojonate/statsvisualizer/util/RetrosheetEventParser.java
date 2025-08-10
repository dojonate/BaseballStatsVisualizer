package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.Play;
import com.dojonate.statsvisualizer.model.RetrosheetGame;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Parser for Retrosheet event files (*.EVN, *.EVA, etc.).
 * It extracts the game id, info lines and every play from the file.
 */
@Component
public class RetrosheetEventParser {

    /**
     * Parses a Retrosheet event file into a {@link RetrosheetGame} representation.
     *
     * @param filePath path to the event file
     * @return parsed game
     * @throws IOException if an IO error occurs
     */
    public RetrosheetGame parseEventFile(Path filePath) throws IOException {
        RetrosheetGame game = new RetrosheetGame();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue; // ignore comments and blank lines
                }

                if (trimmed.startsWith("id,")) {
                    game.setGameId(trimmed.substring(3));
                } else if (trimmed.startsWith("info,")) {
                    String[] parts = trimmed.split(",", 3);
                    if (parts.length == 3) {
                        game.addInfo(parts[1], parts[2]);
                    }
                } else if (trimmed.startsWith("play,")) {
                    parsePlayLine(trimmed).ifPresent(game::addPlay);
                }
            }
        }

        return game;
    }

    private Optional<Play> parsePlayLine(String line) {
        // play,inning,home/visitor,playerID,count,pitches,event
        String[] parts = line.split(",", 7);
        if (parts.length < 7) {
            return Optional.empty();
        }

        try {
            int inning = Integer.parseInt(parts[1]);
            boolean home = "1".equals(parts[2]);
            String playerId = parts[3];
            String count = parts[4];
            String pitches = parts[5];
            String eventField = parts[6];
            int dotIndex = eventField.indexOf('.');
            String event = dotIndex >= 0 ? eventField.substring(0, dotIndex) : eventField;
            Map<Character, String> advances = dotIndex >= 0
                    ? parseRunnerAdvances(eventField.substring(dotIndex + 1))
                    : Map.of();
            return Optional.of(new Play(inning, home, playerId, count, pitches, event, advances));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private Map<Character, String> parseRunnerAdvances(String advancePart) {
        Map<Character, String> advances = new LinkedHashMap<>();
        if (advancePart == null || advancePart.isEmpty()) {
            return advances;
        }
        String[] tokens = advancePart.split(";");
        for (String token : tokens) {
            String trimmed = token.trim();
            if (trimmed.length() >= 3) {
                char from = trimmed.charAt(0);
                char action = trimmed.charAt(1);
                char to = trimmed.charAt(2);
                if (action == '-') {
                    advances.put(from, String.valueOf(to));
                } else if (action == 'X') {
                    advances.put(from, "X" + to);
                }
            }
        }
        return advances;
    }
}
