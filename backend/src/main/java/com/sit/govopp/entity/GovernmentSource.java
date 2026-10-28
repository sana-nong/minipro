package com.sit.govopp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "government_sources")
public class GovernmentSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String agencyCode; // e.g. "UPSC", "SSC", "AICTE", "NSP", "DRDO", "DST"

    @Column(nullable = false)
    private String portalUrl;

    private String feedOrScrapeEndpoint;

    @Column(nullable = false)
    private String aggregationType; // RSS_FEED, WEB_SCRAPER, REST_API

    private LocalDateTime lastSyncTime;

    @Column(nullable = false)
    private String syncStatus; // IDLE, SUCCESS, FAILED, RUNNING

    private int totalOpportunitiesExtracted;

    private String description;

    private boolean enabled = true;

    public GovernmentSource() {
    }

    public GovernmentSource(String name, String agencyCode, String portalUrl, String feedOrScrapeEndpoint,
                            String aggregationType, String description) {
        this.name = name;
        this.agencyCode = agencyCode;
        this.portalUrl = portalUrl;
        this.feedOrScrapeEndpoint = feedOrScrapeEndpoint;
        this.aggregationType = aggregationType;
        this.description = description;
        this.syncStatus = "IDLE";
        this.totalOpportunitiesExtracted = 0;
        this.enabled = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public String getFeedOrScrapeEndpoint() {
        return feedOrScrapeEndpoint;
    }

    public void setFeedOrScrapeEndpoint(String feedOrScrapeEndpoint) {
        this.feedOrScrapeEndpoint = feedOrScrapeEndpoint;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getTotalOpportunitiesExtracted() {
        return totalOpportunitiesExtracted;
    }

    public void setTotalOpportunitiesExtracted(int totalOpportunitiesExtracted) {
        this.totalOpportunitiesExtracted = totalOpportunitiesExtracted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
