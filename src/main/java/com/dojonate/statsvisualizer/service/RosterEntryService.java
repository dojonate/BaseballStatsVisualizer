package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.repository.PlayerRepository;
import com.dojonate.statsvisualizer.repository.RosterEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            // Check if the player already exists
            Optional<Player> existingPlayer = playerRepository.findById(entry.getPlayer().getPlayerId());
            Player player = existingPlayer.orElseGet(() -> playerRepository.save(entry.getPlayer()));

            // Check if the roster entry already exists
            Optional<RosterEntry> existingEntry = rosterEntryRepository.findByPlayerAndTeamAndYear(player, entry.getTeam(), entry.getYear());
            if (existingEntry.isEmpty()) {
                entry.setPlayer(player); // Link the existing player
                rosterEntryRepository.save(entry);
            }
        }
    }

    public Page<RosterEntry> getRosters(String search, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        return rosterEntryRepository.findByPlayerFirstNameContainingOrPlayerLastNameContainingOrTeamNameContaining(search, search, search, pageable);
    }

    public Map<Player, Map<Team, Map.Entry<String, String>>> getConsolidatedRosters(String search, int page, int size, String sortBy, String direction) {
        Page<RosterEntry> rostersPage = getRosters(search, page, size, sortBy, direction);
        List<RosterEntry> rosterEntries = rostersPage.getContent();

        Map<Player, Map<Team, List<RosterEntry>>> groupedEntries = new HashMap<>();

        for (RosterEntry entry : rosterEntries) {
            groupedEntries
                    .computeIfAbsent(entry.getPlayer(), k -> new HashMap<>())
                    .computeIfAbsent(entry.getTeam(), k -> new ArrayList<>())
                    .add(entry);
        }

        Map<Player, Map<Team, Map.Entry<String, String>>> consolidatedEntries = new HashMap<>();

        for (Map.Entry<Player, Map<Team, List<RosterEntry>>> playerEntry : groupedEntries.entrySet()) {
            Map<Team, Map.Entry<String, String>> teamYears = new HashMap<>();
            for (Map.Entry<Team, List<RosterEntry>> teamEntry : playerEntry.getValue().entrySet()) {
                List<Integer> years = teamEntry.getValue().stream().map(RosterEntry::getYear).sorted().toList();
                String position = teamEntry.getValue().get(0).getPosition(); // Assuming position is the same for all entries
                teamYears.put(teamEntry.getKey(), Map.entry(position, consolidateYears(years)));
            }
            consolidatedEntries.put(playerEntry.getKey(), teamYears);
        }

        return consolidatedEntries;
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

//    public List<RosterEntry> findAll() {
//        return rosterEntryRepository.findAll();
//    }
}
