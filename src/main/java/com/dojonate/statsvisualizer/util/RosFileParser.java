package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class RosFileParser {

    public List<RosterEntry> parseRosFile(Path filePath, Team team) throws IOException {
        List<RosterEntry> rosterEntries = new ArrayList<>();

        // Extract the year from the filename (e.g., "2011TEX.ROS")
        String fileName = filePath.getFileName().toString();
        int year = Integer.parseInt(fileName.substring(3, 7)); // Assumes the filename starts with the year

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 7) {
                    // Create a Player object
                    Player player = new Player();
                    player.setPlayerId(fields[0].trim());
                    player.setLastName(fields[1].trim());
                    player.setFirstName(fields[2].trim());
                    player.setBattingHand(fields[3].trim());
                    player.setThrowingHand(fields[4].trim());

                    // Create a RosterEntry object
                    RosterEntry rosterEntry = new RosterEntry();
                    rosterEntry.setPlayer(player);
                    rosterEntry.setTeam(team);
                    rosterEntry.setYear(year);
                    rosterEntry.setPosition(fields[6].trim());

                    rosterEntries.add(rosterEntry);
                } else {
                    System.err.println("Invalid line: " + line);
                }
            }
        }

        return rosterEntries;
    }
}
