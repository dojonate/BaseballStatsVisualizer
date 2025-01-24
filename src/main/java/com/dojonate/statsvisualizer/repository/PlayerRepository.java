package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    // You can define custom queries here if needed
}
