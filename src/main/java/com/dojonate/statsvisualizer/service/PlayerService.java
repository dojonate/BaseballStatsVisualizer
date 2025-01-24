package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.repository.PlayerRepository;
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
        return playerRepository.save(player);
    }

    // Find a player by ID
    public Player findById(String playerId) {
        return playerRepository.findById(playerId).orElse(null);
    }

    // Retrieve all players
    public List<Player> findAll() {
        return playerRepository.findAll();
    }
}
