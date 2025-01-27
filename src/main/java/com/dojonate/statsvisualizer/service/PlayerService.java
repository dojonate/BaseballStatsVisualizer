package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.repository.PlayerRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Save a single player
    public Player save(Player player) {
        return playerRepository.findById(player.getPlayerId())
                .orElseGet(() -> playerRepository.save(player));
    }

    // Find a player by ID
    public Player findById(String playerId) {
        return playerRepository.findById(playerId).orElse(null);
    }

    // Retrieve all players
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Page<Player> getPlayers(String search, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return playerRepository.findByLastNameContainingOrFirstNameContaining(search, search, pageable);
    }
}
