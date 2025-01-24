package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.repository.PlayerRepository;
import com.dojonate.statsvisualizer.repository.RosterEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<RosterEntry> findAll() {
        return rosterEntryRepository.findAll();
    }
}
