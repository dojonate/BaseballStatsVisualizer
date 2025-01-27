package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.repository.PlayerRepository;
import com.dojonate.statsvisualizer.repository.RosterEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RosterEntryService {

    private final RosterEntryRepository rosterEntryRepository;
    private final PlayerRepository playerRepository;

    public RosterEntryService(RosterEntryRepository rosterEntryRepository, PlayerRepository playerRepository) {
        this.rosterEntryRepository = rosterEntryRepository;
        this.playerRepository = playerRepository;
    }

    public void saveAll(List<RosterEntry> rosterEntries) {
        for (RosterEntry entry : rosterEntries) {
            Optional<Player> existingPlayer = playerRepository.findById(entry.getPlayer().getPlayerId());
            Player player = existingPlayer.orElseGet(() -> playerRepository.save(entry.getPlayer()));

            Optional<RosterEntry> existingEntry = rosterEntryRepository.findByPlayerAndTeamAndYear(player, entry.getTeam(), entry.getYear());
            if (existingEntry.isEmpty()) {
                entry.setPlayer(player);
                rosterEntryRepository.save(entry);
            }
        }
    }

    public Page<Map.Entry<Player, Map<Team, Map.Entry<String, String>>>> getConsolidatedRosters(String search, int page, int size, String sortBy, String direction) {
        List<RosterEntry> rosterEntries = rosterEntryRepository.findByPlayerFirstNameContainingOrPlayerLastNameContainingOrTeamNameContaining(search, search, search);

        Map<Player, Map<Team, List<RosterEntry>>> groupedEntries = new HashMap<>();

        for (RosterEntry entry : rosterEntries) {
            groupedEntries
                    .computeIfAbsent(entry.getPlayer(), k -> new HashMap<>())
                    .computeIfAbsent(entry.getTeam(), k -> new ArrayList<>())
                    .add(entry);
        }

        List<Map.Entry<Player, Map<Team, Map.Entry<String, String>>>> consolidatedEntries = new ArrayList<>();

        for (Map.Entry<Player, Map<Team, List<RosterEntry>>> playerEntry : groupedEntries.entrySet()) {
            Map<Team, Map.Entry<String, String>> teamYears = new HashMap<>();
            for (Map.Entry<Team, List<RosterEntry>> teamEntry : playerEntry.getValue().entrySet()) {
                List<Integer> years = teamEntry.getValue().stream().map(RosterEntry::getYear).sorted().toList();
                String position = teamEntry.getValue().get(0).getPosition();
                teamYears.put(teamEntry.getKey(), Map.entry(position, consolidateYears(years)));
            }
            consolidatedEntries.add(Map.entry(playerEntry.getKey(), teamYears));
        }

        // Sort the consolidatedEntries list based on sortBy and direction
        consolidatedEntries.sort((entry1, entry2) -> {
            int comparison = switch (sortBy) {
                case "player.firstName" -> entry1.getKey().getFirstName().compareTo(entry2.getKey().getFirstName());
                case "player.lastName" -> entry1.getKey().getLastName().compareTo(entry2.getKey().getLastName());
                default -> 0;
                // Add more cases if needed
            };
            return "asc".equals(direction) ? comparison : -comparison;
        });

        int start = Math.min(page * size, consolidatedEntries.size());
        int end = Math.min((page + 1) * size, consolidatedEntries.size());
        List<Map.Entry<Player, Map<Team, Map.Entry<String, String>>>> paginatedList = consolidatedEntries.subList(start, end);

        return new PageImpl<>(paginatedList, PageRequest.of(page, size), consolidatedEntries.size());
    }

    private String consolidateYears(List<Integer> years) {
        if (years.isEmpty()) return "";

        List<String> ranges = new ArrayList<>();
        int start = years.get(0);
        int end = start;

        for (int i = 1; i < years.size(); i++) {
            if (years.get(i) == end + 1) {
                end = years.get(i);
            } else {
                if (start == end) {
                    ranges.add(String.valueOf(start));
                } else {
                    ranges.add(start + "-" + end);
                }
                start = years.get(i);
                end = start;
            }
        }

        if (start == end) {
            ranges.add(String.valueOf(start));
        } else {
            ranges.add(start + "-" + end);
        }

        return String.join("; ", ranges);
    }
}