package com.sit.govopp.dto;

import com.sit.govopp.entity.OpportunityCategory;
import com.sit.govopp.entity.OpportunityStatus;

public class OpportunityFilterDto {
    private String query;
    private OpportunityCategory category;
    private String qualification;
    private String location;
    private OpportunityStatus status;
    private Integer closingInDays;
    private Integer maxAge;
    private Double userPercentage;
    private String sortBy = "deadline"; // "deadline", "createdAt", "title"
    private String sortDirection = "ASC"; // "ASC", "DESC"

    public OpportunityFilterDto() {}

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public OpportunityCategory getCategory() {
        return category;
    }

    public void setCategory(OpportunityCategory category) {
        this.category = category;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public Integer getClosingInDays() {
        return closingInDays;
    }

    public void setClosingInDays(Integer closingInDays) {
        this.closingInDays = closingInDays;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Double getUserPercentage() {
        return userPercentage;
    }

    public void setUserPercentage(Double userPercentage) {
        this.userPercentage = userPercentage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
