package com.sit.govopp.entity;

public enum OpportunityStatus {
    ACTIVE("Active"),
    CLOSING_SOON("Closing Soon"),
    EXPIRED("Expired");

    private final String label;

    OpportunityStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
