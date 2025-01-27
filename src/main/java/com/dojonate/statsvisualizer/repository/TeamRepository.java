package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Page<Team> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
