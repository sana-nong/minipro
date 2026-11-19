package com.sit.govopp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "opportunities", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_deadline", columnList = "deadline"),
    @Index(name = "idx_unique_source_id", columnList = "uniqueSourceId")
})
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 200)
    private String organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OpportunityCategory category;

    @Column(nullable = false, length = 200)
    private String qualification;

    @Column(length = 100)
    private String field;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(columnDefinition = "TEXT", length = 5000)
    private String description;

    @Column(columnDefinition = "TEXT", length = 5000)
    private String eligibilityCriteria;

    private Integer minAge;
    private Integer maxAge;
    private Double minPercentage;

    private String applicationFee;

    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OpportunityStatus status;

    @Column(nullable = false, length = 1000)
    private String officialUrl;

    // Direct online registration / apply portal link (e.g. upsconline.nic.in)
    @Column(length = 1000)
    private String directApplyUrl;

    // Direct link to the official notification document / PDF / gazette
    @Column(length = 1000)
    private String notificationPdfUrl;

    // Official advertisement notice reference number (e.g. Advt. No. 04/2025-CDS-I)
    @Column(length = 200)
    private String notificationReference;

    // Raw verbatim deadline clause extracted from the official notification
    @Column(columnDefinition = "TEXT", length = 2000)
    private String extractedDateSnippet;

    // Raw verbatim statutory eligibility clause extracted from the official notice
    @Column(columnDefinition = "TEXT", length = 2000)
    private String extractedEligibilitySnippet;

    // Specific portal or platform name (e.g. "UPSC OTR Portal (upsconline.nic.in)")
    @Column(length = 250)
    private String portalName;

    private Boolean newlySynced = false;

    @Column(nullable = false)
    private Boolean isLiveScraped = false;

    @Column(nullable = false, length = 100)
    private String sourceAgency;

    @Column(unique = true, length = 255)
    private String uniqueSourceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (uniqueSourceId != null && uniqueSourceId.contains("LIVE")) {
            this.isLiveScraped = true;
        }
        refreshStatus();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (uniqueSourceId != null && uniqueSourceId.contains("LIVE")) {
            this.isLiveScraped = true;
        }
        refreshStatus();
    }

    public void refreshStatus() {
        if (deadline == null) {
            this.status = OpportunityStatus.ACTIVE;
            return;
        }
        LocalDate today = LocalDate.now();
        if (deadline.isBefore(today)) {
            this.status = OpportunityStatus.EXPIRED;
        } else {
            long days = ChronoUnit.DAYS.between(today, deadline);
            if (days <= 7) {
                this.status = OpportunityStatus.CLOSING_SOON;
            } else {
                this.status = OpportunityStatus.ACTIVE;
            }
        }
    }

    @Transient
    public long getDaysRemaining() {
        if (deadline == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    public Opportunity() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Double getMinPercentage() {
        return minPercentage;
    }

    public void setMinPercentage(Double minPercentage) {
        this.minPercentage = minPercentage;
    }

    public String getApplicationFee() {
        return applicationFee;
    }

    public void setApplicationFee(String applicationFee) {
        this.applicationFee = applicationFee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public String getDirectApplyUrl() {
        return directApplyUrl != null && !directApplyUrl.isEmpty() ? directApplyUrl : officialUrl;
    }

    public void setDirectApplyUrl(String directApplyUrl) {
        this.directApplyUrl = directApplyUrl;
    }

    public String getNotificationPdfUrl() {
        return notificationPdfUrl;
    }

    public void setNotificationPdfUrl(String notificationPdfUrl) {
        this.notificationPdfUrl = notificationPdfUrl;
    }

    public String getNotificationReference() {
        return notificationReference;
    }

    public void setNotificationReference(String notificationReference) {
        this.notificationReference = notificationReference;
    }

    public String getExtractedDateSnippet() {
        return extractedDateSnippet;
    }

    public void setExtractedDateSnippet(String extractedDateSnippet) {
        this.extractedDateSnippet = extractedDateSnippet;
    }

    public String getExtractedEligibilitySnippet() {
        return extractedEligibilitySnippet;
    }

    public void setExtractedEligibilitySnippet(String extractedEligibilitySnippet) {
        this.extractedEligibilitySnippet = extractedEligibilitySnippet;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public Boolean getNewlySynced() {
        return newlySynced;
    }

    public void setNewlySynced(Boolean newlySynced) {
        this.newlySynced = newlySynced;
    }

    public Boolean getIsLiveScraped() {
        return Boolean.TRUE.equals(isLiveScraped) || (uniqueSourceId != null && uniqueSourceId.contains("LIVE"));
    }

    public void setIsLiveScraped(Boolean isLiveScraped) {
        this.isLiveScraped = isLiveScraped;
    }

    public String getSourceAgency() {
        return sourceAgency;
    }

    public void setSourceAgency(String sourceAgency) {
        this.sourceAgency = sourceAgency;
    }

    public String getUniqueSourceId() {
        return uniqueSourceId;
    }

    public void setUniqueSourceId(String uniqueSourceId) {
        this.uniqueSourceId = uniqueSourceId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
