package com.sit.govopp.service;

import com.sit.govopp.dto.EligibilityCheckDto;
import com.sit.govopp.dto.OpportunityFilterDto;
import com.sit.govopp.dto.StatsSummaryDto;
import com.sit.govopp.entity.Opportunity;
import com.sit.govopp.entity.OpportunityCategory;
import com.sit.govopp.entity.OpportunityStatus;
import com.sit.govopp.repository.GovernmentSourceRepository;
import com.sit.govopp.repository.OpportunityRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class SearchFilterService {

    private final OpportunityRepository opportunityRepository;
    private final GovernmentSourceRepository sourceRepository;

    public SearchFilterService(OpportunityRepository opportunityRepository,
                               GovernmentSourceRepository sourceRepository) {
        this.opportunityRepository = opportunityRepository;
        this.sourceRepository = sourceRepository;
    }

    /**
     * Search and Filter opportunities matching dynamic user criteria (Objective 2)
     */
    @Transactional(readOnly = true)
    public List<Opportunity> searchAndFilter(OpportunityFilterDto filter) {
        Specification<Opportunity> spec = buildSpecification(filter);

        Sort sort = buildSort(filter);
        List<Opportunity> results = opportunityRepository.findAll(spec, sort);

        // Dynamically compute days remaining and status
        results.forEach(Opportunity::refreshStatus);
        return results;
    }

    private Specification<Opportunity> buildSpecification(OpportunityFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getQuery() != null && !filter.getQuery().trim().isEmpty()) {
                String searchPattern = "%" + filter.getQuery().trim().toLowerCase() + "%";
                Predicate titlePred = cb.like(cb.lower(root.get("title")), searchPattern);
                Predicate orgPred = cb.like(cb.lower(root.get("organization")), searchPattern);
                Predicate agencyPred = cb.like(cb.lower(root.get("sourceAgency")), searchPattern);
                Predicate descPred = cb.like(cb.lower(root.get("description")), searchPattern);
                Predicate qualPred = cb.like(cb.lower(root.get("qualification")), searchPattern);
                Predicate fieldPred = cb.like(cb.lower(root.get("field")), searchPattern);
                Predicate refPred = cb.like(cb.lower(root.get("notificationReference")), searchPattern);
                Predicate portalPred = cb.like(cb.lower(root.get("portalName")), searchPattern);

                predicates.add(cb.or(titlePred, orgPred, agencyPred, descPred, qualPred, fieldPred, refPred, portalPred));
            }

            if (filter.getCategory() != null) {
                predicates.add(cb.equal(root.get("category"), filter.getCategory()));
            }

            if (filter.getStatus() != null) {
                if (filter.getStatus() == OpportunityStatus.ACTIVE) {
                    predicates.add(cb.or(
                            cb.equal(root.get("status"), OpportunityStatus.ACTIVE),
                            cb.equal(root.get("status"), OpportunityStatus.CLOSING_SOON)
                    ));
                    predicates.add(cb.greaterThanOrEqualTo(root.get("deadline"), LocalDate.now()));
                } else if (filter.getStatus() == OpportunityStatus.CLOSING_SOON) {
                    predicates.add(cb.equal(root.get("status"), OpportunityStatus.CLOSING_SOON));
                    predicates.add(cb.greaterThanOrEqualTo(root.get("deadline"), LocalDate.now()));
                } else if (filter.getStatus() == OpportunityStatus.EXPIRED) {
                    predicates.add(cb.or(
                            cb.equal(root.get("status"), OpportunityStatus.EXPIRED),
                            cb.lessThan(root.get("deadline"), LocalDate.now())
                    ));
                }
            }

            if (filter.getNewlySynced() != null && filter.getNewlySynced()) {
                predicates.add(cb.equal(root.get("newlySynced"), true));
            }

            if (filter.getLiveOnly() != null && filter.getLiveOnly()) {
                predicates.add(cb.or(
                        cb.equal(root.get("isLiveScraped"), true),
                        cb.like(root.get("uniqueSourceId"), "%LIVE%")
                ));
            }

            if (filter.getQualification() != null && !filter.getQualification().trim().isEmpty()
                    && !"ALL".equalsIgnoreCase(filter.getQualification())) {
                predicates.add(cb.like(cb.lower(root.get("qualification")), "%" + filter.getQualification().trim().toLowerCase() + "%"));
            }

            if (filter.getLocation() != null && !filter.getLocation().trim().isEmpty()
                    && !"ALL".equalsIgnoreCase(filter.getLocation())) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + filter.getLocation().trim().toLowerCase() + "%"));
            }

            if (filter.getClosingInDays() != null && filter.getClosingInDays() > 0) {
                LocalDate maxDeadline = LocalDate.now().plusDays(filter.getClosingInDays());
                predicates.add(cb.between(root.get("deadline"), LocalDate.now(), maxDeadline));
            }

            if (filter.getMaxAge() != null && filter.getMaxAge() > 0) {
                predicates.add(cb.or(
                        cb.isNull(root.get("maxAge")),
                        cb.greaterThanOrEqualTo(root.get("maxAge"), filter.getMaxAge())
                ));
            }

            if (filter.getUserPercentage() != null && filter.getUserPercentage() > 0) {
                predicates.add(cb.or(
                        cb.isNull(root.get("minPercentage")),
                        cb.lessThanOrEqualTo(root.get("minPercentage"), filter.getUserPercentage())
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort buildSort(OpportunityFilterDto filter) {
        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "deadline";
        Sort.Direction direction = "DESC".equalsIgnoreCase(filter.getSortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        if ("live".equalsIgnoreCase(sortBy) || "liveFirst".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "isLiveScraped").and(Sort.by(direction, "deadline"));
        } else if ("deadline".equalsIgnoreCase(sortBy)) {
            return Sort.by(direction, "deadline");
        } else if ("createdAt".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        } else if ("title".equalsIgnoreCase(sortBy)) {
            return Sort.by(direction, "title");
        }
        return Sort.by(Sort.Direction.ASC, "deadline");
    }

    /**
     * Compute real-time dashboard analytics
     */
    @Transactional(readOnly = true)
    public StatsSummaryDto getDashboardStats() {
        List<Opportunity> all = opportunityRepository.findAll();
        all.forEach(Opportunity::refreshStatus);

        long total = all.size();
        long active = all.stream().filter(o -> o.getStatus() == OpportunityStatus.ACTIVE).count();
        long closingSoon = all.stream().filter(o -> o.getStatus() == OpportunityStatus.CLOSING_SOON).count();
        long expired = all.stream().filter(o -> o.getStatus() == OpportunityStatus.EXPIRED).count();
        long totalSources = sourceRepository.count();
        long liveScraped = all.stream().filter(o -> Boolean.TRUE.equals(o.getIsLiveScraped()) || (o.getUniqueSourceId() != null && o.getUniqueSourceId().contains("LIVE"))).count();

        Map<String, Long> categoryMap = new HashMap<>();
        for (OpportunityCategory cat : OpportunityCategory.values()) {
            long count = all.stream().filter(o -> o.getCategory() == cat).count();
            categoryMap.put(cat.name(), count);
        }

        Map<String, Long> statusMap = new HashMap<>();
        statusMap.put("ACTIVE", active);
        statusMap.put("CLOSING_SOON", closingSoon);
        statusMap.put("EXPIRED", expired);

        return new StatsSummaryDto(total, active, closingSoon, expired, totalSources, liveScraped, categoryMap, statusMap);
    }

    /**
     * Automatic Candidate-Opportunity Statutory Rule Evaluation (Objective 3)
     */
    public EligibilityCheckDto.Result evaluateEligibility(Long opportunityId, EligibilityCheckDto candidateProfile) {
        Opportunity opp = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found with ID: " + opportunityId));

        boolean eligible = true;
        List<String> matchReasons = new ArrayList<>();
        List<String> mismatchReasons = new ArrayList<>();

        // 1. Age Evaluation
        if (candidateProfile.getAge() != null) {
            if (opp.getMinAge() != null && candidateProfile.getAge() < opp.getMinAge()) {
                eligible = false;
                mismatchReasons.add("Age criteria failed: Candidate age (" + candidateProfile.getAge() +
                        " yrs) is less than the statutory minimum requirement of " + opp.getMinAge() + " yrs.");
            } else if (opp.getMaxAge() != null && candidateProfile.getAge() > opp.getMaxAge()) {
                eligible = false;
                mismatchReasons.add("Age criteria failed: Candidate age (" + candidateProfile.getAge() +
                        " yrs) exceeds the statutory maximum age limit of " + opp.getMaxAge() + " yrs.");
            } else {
                matchReasons.add("Age criteria satisfied: Candidate age fits permissible window (" +
                        (opp.getMinAge() != null ? opp.getMinAge() : "18") + " to " +
                        (opp.getMaxAge() != null ? opp.getMaxAge() : "Any") + " yrs).");
            }
        }

        // 2. Minimum Percentage Evaluation
        if (candidateProfile.getPercentage() != null) {
            if (opp.getMinPercentage() != null && candidateProfile.getPercentage() < opp.getMinPercentage()) {
                eligible = false;
                mismatchReasons.add("Academic criteria failed: Aggregate percentage (" +
                        candidateProfile.getPercentage() + "%) is below the statutory requirement of " +
                        opp.getMinPercentage() + "%.");
            } else {
                matchReasons.add("Academic score satisfied: Score meets or exceeds statutory threshold (" +
                        (opp.getMinPercentage() != null ? opp.getMinPercentage() + "%" : "Pass criteria") + ").");
            }
        }

        // 3. Qualification Match (Semantic token matching)
        if (candidateProfile.getQualification() != null && !candidateProfile.getQualification().isEmpty()) {
            String candidateQual = candidateProfile.getQualification().toLowerCase();
            String oppQual = opp.getQualification().toLowerCase();

            boolean streamMatched = false;
            String[] keywords = {"b.tech", "b.e", "m.tech", "m.sc", "computer science", "electronics", "civil", "mechanical", "diploma", "degree", "graduate", "mca"};
            for (String kw : keywords) {
                if (candidateQual.contains(kw) && oppQual.contains(kw)) {
                    streamMatched = true;
                    break;
                }
            }

            if (streamMatched || oppQual.contains("any") || oppQual.contains("recognized university")) {
                matchReasons.add("Educational background matches target recruitment qualification: " + opp.getQualification());
            } else {
                matchReasons.add("Candidate qualification evaluated against: " + opp.getQualification());
            }
        }

        return new EligibilityCheckDto.Result(opp.getId(), opp.getTitle(), eligible, matchReasons, mismatchReasons);
    }
}
