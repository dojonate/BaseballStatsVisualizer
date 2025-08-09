package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Game;
import com.dojonate.statsvisualizer.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class GamesViewController {

    @Autowired
    private GameService gameService;

    /**
     * List games filtered by team and year.
     * Only shows games if both filters are provided.
     */
    @GetMapping("/games")
    public String listGames(
            @RequestParam(defaultValue = "") String team,
            @RequestParam(defaultValue = "") String year,
            Model model) {

        // Only perform filtering if both team and year are provided.
        if (team.isEmpty() || year.isEmpty()) {
            model.addAttribute("games", Collections.emptyList());
            model.addAttribute("error", "Please provide both a team name and a year to view games.");
            model.addAttribute("team", team);
            model.addAttribute("year", year);
            return "games";
        }

        // Retrieve all games and then filter based on both team and year.
        List<Game> games = gameService.findAll();

        // Filter by team name (matches against home and away teams)
        games.removeIf(game ->
                (game.getHomeTeam() == null || !game.getHomeTeam().getName().toLowerCase().contains(team.toLowerCase())) &&
                        (game.getAwayTeam() == null || !game.getAwayTeam().getName().toLowerCase().contains(team.toLowerCase()))
        );

        // Filter by year (using the year from the game date)
        games.removeIf(game -> {
            int gameYear = game.getDate().get(java.util.Calendar.YEAR);
            try {
                return gameYear != Integer.parseInt(year);
            } catch (NumberFormatException e) {
                return true;
            }
        });

        model.addAttribute("games", games);
        model.addAttribute("team", team);
        model.addAttribute("year", year);
        return "games";
    }

    /**
     * Display a single game’s details.
     */
    @GetMapping("/game/{id}")
    public String viewGame(@PathVariable String id, Model model) {
        Game game = gameService.findById(id).orElse(null);
        if (game == null) {
            return "redirect:/games?error=GameNotFound";
        }
        model.addAttribute("game", game);
        return "game-details";
    }
}
