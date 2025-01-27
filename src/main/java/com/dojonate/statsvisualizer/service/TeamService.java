package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Team;
import com.dojonate.statsvisualizer.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    // Find or create a team by team ID
    public Team findOrCreateTeam(String teamId, String defaultName, String defaultLeague) {
        return teamRepository.findById(teamId)
                .orElseGet(() -> teamRepository.save(new Team(teamId, defaultName, defaultLeague, 0, 0)));
    }

    public void saveAll(List<Team> teams) {
        for (Team team : teams) {
            teamRepository.findById(team.getTeamId()).ifPresentOrElse(existingTeam -> {
                // Update league, name, firstYear, and lastYear if necessary
                existingTeam.setLeague(team.getLeague());
                existingTeam.setName(team.getName());
                existingTeam.setFirstYear(team.getFirstYear());
                existingTeam.setLastYear(team.getLastYear());
                teamRepository.save(existingTeam);
            }, () ->
            {
                // Save new team if it doesn't exist
                teamRepository.save(team);
            });
        }
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    // Paginated and sortable retrieval of teams
    public Page<Team> getTeams(String search, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size,
                "asc".equalsIgnoreCase(direction)
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending());
        if (search != null && !search.isEmpty()) {
            return teamRepository.findByNameContainingIgnoreCase(search, pageable);
        }
        return teamRepository.findAll(pageable);
    }
}
