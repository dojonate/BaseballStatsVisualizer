package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.service.RosterEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RostersViewController {

    private final RosterEntryService rosterEntryService;

    public RostersViewController(RosterEntryService rosterEntryService) {
        this.rosterEntryService = rosterEntryService;
    }

    @GetMapping("/rosters")
    public String listRosters(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "player.lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        Map<Player, Map<Team, String>> consolidatedRosters = rosterEntryService.getConsolidatedRosters(search, page - 1, size, sortBy, direction);
        model.addAttribute("consolidatedRosters", consolidatedRosters);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 1); // Adjust as needed
        model.addAttribute("search", search);
        return "rosters";
    }
}