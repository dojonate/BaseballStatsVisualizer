package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RosterEntry;
import com.dojonate.statsvisualizer.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface RosterEntryRepository extends JpaRepository<RosterEntry, Long> {
    Optional<RosterEntry> findByPlayerAndTeamAndYear(Player player, Team team, int year);
}
