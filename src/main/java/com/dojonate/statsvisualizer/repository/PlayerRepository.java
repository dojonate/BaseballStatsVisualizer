package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    // You can define custom queries here if needed
}
