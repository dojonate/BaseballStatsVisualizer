package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RosterEntryRepository extends JpaRepository<RosterEntry, Long> {
    List<RosterEntry> findByPlayerFirstNameContainingOrPlayerLastNameContainingOrTeamNameContaining(String playerFirstName, String playerLastName, String teamName);

    Optional<RosterEntry> findByPlayerAndTeamAndYear(Player player, Team team, int year);
}