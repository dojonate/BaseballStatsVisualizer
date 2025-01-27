package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TeamsViewController {

    private final TeamService teamService;

    public TeamsViewController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams")
    public String listTeams(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        Page<Team> teamsPage = teamService.getTeams(search, page - 1, size, sortBy, direction);
        model.addAttribute("teams", teamsPage.getContent());
        model.addAttribute("currentPage", teamsPage.getNumber() + 1);
        model.addAttribute("totalPages", teamsPage.getTotalPages());
        model.addAttribute("search", search);
        return "teams";
    }
}
