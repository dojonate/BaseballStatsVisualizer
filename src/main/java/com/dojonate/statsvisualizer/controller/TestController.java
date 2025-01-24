package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestController {

    private final PlayerService playerService;

    public TestController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/test-db")
    public String testDatabase() {
        return "Player count: " + playerService.findAll().size();
    }

    @GetMapping("/players")
    public String getAllPlayers(Model model) {
        List<Player> players = playerService.findAll();
        model.addAttribute("players", players);
        return "players";
    }
}
