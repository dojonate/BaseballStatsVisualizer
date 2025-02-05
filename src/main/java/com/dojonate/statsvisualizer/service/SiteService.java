package com.dojonate.statsvisualizer.service;

import com.dojonate.statsvisualizer.model.Site;
import com.dojonate.statsvisualizer.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {
    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    // Save a single site
    public Site save(Site site) {
        return siteRepository.findById(site.getSiteId())
                .orElseGet(() -> siteRepository.save(site));
    }

    // Find a site by ID
    public Site findById(String siteId) {
        return siteRepository.findById(siteId).orElse(null);
    }

    // Retrieve all sites
    public List<Site> findAll() {
        return siteRepository.findAll();
    }
}
