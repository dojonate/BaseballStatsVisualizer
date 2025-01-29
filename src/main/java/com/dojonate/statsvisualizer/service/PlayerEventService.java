package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.PlayerEvent;
import com.dojonate.statsvisualizer.repository.PlayerEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerEventService {

    private final PlayerEventRepository playerEventRepository;

    public PlayerEventService(PlayerEventRepository playerEventRepository) {
        this.playerEventRepository = playerEventRepository;
    }

    public PlayerEvent save(PlayerEvent playerEvent) {
        return playerEventRepository.save(playerEvent);
    }

    public List<PlayerEvent> saveAll(List<PlayerEvent> playerEvents) {
        return playerEventRepository.saveAll(playerEvents);
    }

    public List<PlayerEvent> findAll() {
        return playerEventRepository.findAll();
    }

    public Optional<PlayerEvent> findById(Long id) {
        return playerEventRepository.findById(id);
    }

    public void deleteById(Long id) {
        playerEventRepository.deleteById(id);
    }
}