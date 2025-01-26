package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.service.PlayerService;
import com.dojonate.statsvisualizer.service.RosterEntryService;
import com.dojonate.statsvisualizer.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SampleController {

    private final PlayerService playerService;
    private final RosterEntryService rosterEntryService;
    private final TeamService teamService;

    public SampleController(PlayerService playerService, RosterEntryService rosterEntryService, TeamService teamService) {
        this.playerService = playerService;
        this.rosterEntryService = rosterEntryService;
        this.teamService = teamService;
    }

    @GetMapping("/test-db")
    public String testDatabase() {
        return "Player count: " + playerService.findAll().size();
    }

//    @GetMapping("/players")
//    public String getAllPlayers(Model model) {
//        List<Player> players = playerService.findAll();
//        model.addAttribute("players", players);
//        return "players";
//    }

    @GetMapping("/rosters")
    public String getAllRosters(Model model) {
        List<RosterEntry> rosters = rosterEntryService.findAll();
        model.addAttribute("rosters", rosters);
        return "rosters";
    }

    @GetMapping("/teams")
    public String getAllTeams(Model model) {
        List<Team> teams = teamService.findAll();
        model.addAttribute("teams", teams);
        return "teams";
    }
}
