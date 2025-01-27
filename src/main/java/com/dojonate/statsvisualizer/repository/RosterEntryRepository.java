package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RosterEntryRepository extends JpaRepository<RosterEntry, Long> {
    Page<RosterEntry> findByPlayerFirstNameContainingOrPlayerLastNameContainingOrTeamNameContaining(String playerFirstName, String playerLastName, String teamName, Pageable pageable);

    Optional<RosterEntry> findByPlayerAndTeamAndYear(Player player, Team team, int year);
}