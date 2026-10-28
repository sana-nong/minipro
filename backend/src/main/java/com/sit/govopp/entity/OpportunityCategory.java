package com.sit.govopp.entity;

public enum OpportunityCategory {
    RECRUITMENT("Recruitment & Jobs"),
    SCHOLARSHIP("Scholarships & Financial Aid"),
    FELLOWSHIP("Research Fellowships"),
    INTERNSHIP("Government Internships"),
    COMPETITIVE_EXAM("Competitive Examinations");

    private final String displayName;

    OpportunityCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
