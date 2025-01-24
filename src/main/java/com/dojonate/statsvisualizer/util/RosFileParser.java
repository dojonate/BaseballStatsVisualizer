package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.Player;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class RosFileParser {

    public List<Player> parseRosFile(Path filePath) throws IOException {
        List<Player> players = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Parsing line: " + line); // Log each line

                // Split each line into fields
                String[] fields = line.split(",");

                if (fields.length >= 7) {
                    Player player = new Player();
                    player.setPlayerId(fields[0].trim());
                    player.setLastName(fields[1].trim());
                    player.setFirstName(fields[2].trim());
                    System.out.println("successfully input player name: " + player.getLastName() +
                            ", " + player.getFirstName() + " from " + fields[1] + ", " + fields[2]);
                    player.setBattingHand(fields[3].trim());
                    player.setThrowingHand(fields[4].trim());
                    player.setTeam(fields[5].trim());
                    player.setPosition(fields[6].trim());
                    players.add(player);
                } else {
                    System.err.println("Invalid line: " + line); // Log invalid lines
                }
            }
        }

        return players;
    }

    private LocalDate parseDate(String year, String month, String day) {
        try {
            return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        } catch (Exception e) {
            // Return null if date parsing fails (e.g., incomplete data)
            return null;
        }
    }
}
