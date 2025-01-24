package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface TeamRepository extends JpaRepository<Team, String> {
}
