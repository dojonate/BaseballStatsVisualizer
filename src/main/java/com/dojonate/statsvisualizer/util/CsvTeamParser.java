package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.Team;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvTeamParser {

    public List<Team> parseTeamCsv(Path filePath) throws IOException {
        List<Team> teams = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            // Read the header line and skip it
            if ((line = reader.readLine()) != null) {
                System.out.println("Skipping header row: " + line); // Debug log for clarity
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String teamId = fields[0].trim();
                    String league = fields[1].trim();
                    String name = fields[2].trim() + " " + fields[3].trim(); // Combine CITY + NICKNAME
                    int firstYear = Integer.parseInt(fields[4].trim());
                    int lastYear = Integer.parseInt(fields[5].trim());

                    teams.add(new Team(teamId, name, league, firstYear, lastYear));
                } else {
                    System.err.println("Invalid line: " + line);
                }
            }
        }

        return teams;
    }
}
