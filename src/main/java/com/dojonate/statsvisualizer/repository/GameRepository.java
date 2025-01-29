// src/main/java/com/dojonate/statsvisualizer/repository/GameRepository.java
package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {
}