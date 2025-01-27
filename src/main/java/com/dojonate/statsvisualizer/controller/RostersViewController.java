package com.dojonate.statsvisualizer.controller;

import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.service.RosterEntryService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(defaultValue = "playerName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        Page<RosterEntry> rostersPage = rosterEntryService.getRosters(search, page - 1, size, sortBy, direction);
        model.addAttribute("rosters", rostersPage.getContent());
        model.addAttribute("currentPage", rostersPage.getNumber() + 1);
        model.addAttribute("totalPages", rostersPage.getTotalPages());
        model.addAttribute("search", search);
        return "rosters";
    }
}