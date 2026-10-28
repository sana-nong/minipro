package com.sit.govopp.dto;

import java.util.Map;

public class StatsSummaryDto {
    private long totalOpportunities;
    private long activeCount;
    private long closingSoonCount;
    private long expiredCount;
    private long totalSourcesMonitored;
    private Map<String, Long> categoryBreakdown;
    private Map<String, Long> statusBreakdown;

    public StatsSummaryDto() {}

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
