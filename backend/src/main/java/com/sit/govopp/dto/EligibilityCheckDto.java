package com.sit.govopp.dto;

import java.util.List;

public class EligibilityCheckDto {
    private String qualification; // e.g. "B.Tech", "Degree", "12th"
    private Integer age;
    private Double percentage;
    private String category; // General, OBC, SC, ST, EWS

    public EligibilityCheckDto() {}

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static class Result {
        private Long opportunityId;
        private String title;
        private boolean eligible;
        private List<String> matchReasons;
        private List<String> mismatchReasons;

        public Result() {}

        public Result(Long opportunityId, String title, boolean eligible, List<String> matchReasons, List<String> mismatchReasons) {
            this.opportunityId = opportunityId;
            this.title = title;
            this.eligible = eligible;
            this.matchReasons = matchReasons;
            this.mismatchReasons = mismatchReasons;
        }

        public Long getOpportunityId() {
            return opportunityId;
        }

        public void setOpportunityId(Long opportunityId) {
            this.opportunityId = opportunityId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isEligible() {
            return eligible;
        }

        public void setEligible(boolean eligible) {
            this.eligible = eligible;
        }

        public List<String> getMatchReasons() {
            return matchReasons;
        }

        public void setMatchReasons(List<String> matchReasons) {
            this.matchReasons = matchReasons;
        }

        public List<String> getMismatchReasons() {
            return mismatchReasons;
        }

        public void setMismatchReasons(List<String> mismatchReasons) {
            this.mismatchReasons = mismatchReasons;
        }
    }
}
