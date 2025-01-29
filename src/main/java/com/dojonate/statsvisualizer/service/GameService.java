package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Game;
import com.dojonate.statsvisualizer.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Optional<Game> findById(String id) {
        return gameRepository.findById(id);
    }

    public void deleteById(String id) {
        gameRepository.deleteById(id);
    }
}