package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PlayersViewController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public String listPlayers(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Page<Player> playerPage = playerService.getPlayers(search, page, size, sortBy, direction);
        int totalPages = playerPage.getTotalPages();
        int currentPage = playerPage.getNumber();
        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 3);

        List<Integer> pages = IntStream.range(start, end)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("players", playerPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pages", pages);
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "players";
    }
}
