// src/main/java/com/dojonate/statsvisualizer/repository/PlayerEventRepository.java
package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.PlayerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerEventRepository extends JpaRepository<PlayerEvent, Long> {
}