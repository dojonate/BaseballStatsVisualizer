package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Player;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    Page<Player> findByLastNameContainingOrFirstNameContaining(String lastName, String firstName, Pageable pageable);
}
