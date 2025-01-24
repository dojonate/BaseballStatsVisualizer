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
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String teamId = fields[0].trim();
                    String league = fields[1].trim();
                    String name = fields[2].trim() + " " + fields[3].trim(); // CITY + NICKNAME
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
