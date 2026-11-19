package com.sit.govopp.dto;

import java.util.Map;

public class StatsSummaryDto {
    private long totalOpportunities;
    private long activeCount;
    private long closingSoonCount;
    private long expiredCount;
    private long totalSourcesMonitored;
    private long liveScrapedCount;
    private Map<String, Long> categoryBreakdown;
    private Map<String, Long> statusBreakdown;

    public StatsSummaryDto() {}

    public StatsSummaryDto(long totalOpportunities, long activeCount, long closingSoonCount, long expiredCount,
                           long totalSourcesMonitored, long liveScrapedCount, Map<String, Long> categoryBreakdown, Map<String, Long> statusBreakdown) {
        this.totalOpportunities = totalOpportunities;
        this.activeCount = activeCount;
        this.closingSoonCount = closingSoonCount;
        this.expiredCount = expiredCount;
        this.totalSourcesMonitored = totalSourcesMonitored;
        this.liveScrapedCount = liveScrapedCount;
        this.categoryBreakdown = categoryBreakdown;
        this.statusBreakdown = statusBreakdown;
    }

    public long getLiveScrapedCount() {
        return liveScrapedCount;
    }

    public void setLiveScrapedCount(long liveScrapedCount) {
        this.liveScrapedCount = liveScrapedCount;
    }

    public long getTotalOpportunities() {
        return totalOpportunities;
    }

    public void setTotalOpportunities(long totalOpportunities) {
        this.totalOpportunities = totalOpportunities;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public long getClosingSoonCount() {
        return closingSoonCount;
    }

    public void setClosingSoonCount(long closingSoonCount) {
        this.closingSoonCount = closingSoonCount;
    }

    public long getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(long expiredCount) {
        this.expiredCount = expiredCount;
    }

    public long getTotalSourcesMonitored() {
        return totalSourcesMonitored;
    }

    public void setTotalSourcesMonitored(long totalSourcesMonitored) {
        this.totalSourcesMonitored = totalSourcesMonitored;
    }

    public Map<String, Long> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, Long> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public Map<String, Long> getStatusBreakdown() {
        return statusBreakdown;
    }

    public void setStatusBreakdown(Map<String, Long> statusBreakdown) {
        this.statusBreakdown = statusBreakdown;
    }
}
