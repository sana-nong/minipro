package com.sit.govopp.dto;

import java.time.LocalDateTime;

public class SyncResultDto {
    private String sourceName;
    private String agencyCode;
    private int itemsFound;
    private int newAdded;
    private int updated;
    private String status; // SUCCESS, FAILED
    private String message;
    private long executionTimeMs;
    private LocalDateTime timestamp;

    public SyncResultDto() {
        this.timestamp = LocalDateTime.now();
    }

    public SyncResultDto(String sourceName, String agencyCode, int itemsFound, int newAdded, int updated,
                         String status, String message, long executionTimeMs) {
        this.sourceName = sourceName;
        this.agencyCode = agencyCode;
        this.itemsFound = itemsFound;
        this.newAdded = newAdded;
        this.updated = updated;
        this.status = status;
        this.message = message;
        this.executionTimeMs = executionTimeMs;
        this.timestamp = LocalDateTime.now();
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public int getItemsFound() {
        return itemsFound;
    }

    public void setItemsFound(int itemsFound) {
        this.itemsFound = itemsFound;
    }

    public int getNewAdded() {
        return newAdded;
    }

    public void setNewAdded(int newAdded) {
        this.newAdded = newAdded;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
