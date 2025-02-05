package com.dojonate.statsvisualizer.repository;

import com.dojonate.statsvisualizer.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, String> {
}
