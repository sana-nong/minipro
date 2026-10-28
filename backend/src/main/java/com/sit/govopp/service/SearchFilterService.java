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
                Predicate descPred = cb.like(cb.lower(root.get("description")), searchPattern);
                Predicate qualPred = cb.like(cb.lower(root.get("qualification")), searchPattern);
                Predicate fieldPred = cb.like(cb.lower(root.get("field")), searchPattern);

                predicates.add(cb.or(titlePred, orgPred, descPred, qualPred, fieldPred));
            }

            if (filter.getCategory() != null) {
                predicates.add(cb.equal(root.get("category"), filter.getCategory()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
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
        Sort.Direction direction = "DESC".equalsIgnoreCase(filter.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        switch (sortBy.toLowerCase()) {
            case "createdat":
                return Sort.by(direction, "createdAt");
            case "title":
                return Sort.by(direction, "title");
            case "deadline":
            default:
                return Sort.by(direction, "deadline");
        }
    }

    /**
     * Compute real-time dashboard analytics
     */
    @Transactional(readOnly = true)
    public StatsSummaryDto getDashboardStats() {
        StatsSummaryDto stats = new StatsSummaryDto();

        List<Opportunity> all = opportunityRepository.findAll();
        all.forEach(Opportunity::refreshStatus);

        stats.setTotalOpportunities(all.size());

        long active = all.stream().filter(o -> o.getStatus() == OpportunityStatus.ACTIVE).count();
        long closingSoon = all.stream().filter(o -> o.getStatus() == OpportunityStatus.CLOSING_SOON).count();
        long expired = all.stream().filter(o -> o.getStatus() == OpportunityStatus.EXPIRED).count();

        stats.setActiveCount(active);
        stats.setClosingSoonCount(closingSoon);
        stats.setExpiredCount(expired);
        stats.setTotalSourcesMonitored(sourceRepository.count());

        Map<String, Long> categoryCounts = new HashMap<>();
        for (OpportunityCategory cat : OpportunityCategory.values()) {
            long count = all.stream().filter(o -> o.getCategory() == cat).count();
            categoryCounts.put(cat.name(), count);
        }
        stats.setCategoryBreakdown(categoryCounts);

        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("ACTIVE", active);
        statusCounts.put("CLOSING_SOON", closingSoon);
        statusCounts.put("EXPIRED", expired);
        stats.setStatusBreakdown(statusCounts);

        return stats;
    }

    /**
     * Objective 2: Checks candidate eligibility against opportunity requirements
     */
    public EligibilityCheckDto.Result evaluateEligibility(Long opportunityId, EligibilityCheckDto candidate) {
        Opportunity opp = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found with id: " + opportunityId));

        List<String> matchReasons = new ArrayList<>();
        List<String> mismatchReasons = new ArrayList<>();
        boolean eligible = true;

        // 1. Age check
        if (candidate.getAge() != null) {
            if (opp.getMinAge() != null && candidate.getAge() < opp.getMinAge()) {
                eligible = false;
                mismatchReasons.add("Age below minimum required (" + opp.getMinAge() + " yrs). You are " + candidate.getAge() + ".");
            } else if (opp.getMaxAge() != null && candidate.getAge() > opp.getMaxAge()) {
                eligible = false;
                mismatchReasons.add("Age exceeds maximum permitted (" + opp.getMaxAge() + " yrs). You are " + candidate.getAge() + ".");
            } else {
                matchReasons.add("Age (" + candidate.getAge() + " yrs) satisfies the required age bracket.");
            }
        }

        // 2. Percentage check
        if (candidate.getPercentage() != null && opp.getMinPercentage() != null) {
            if (candidate.getPercentage() < opp.getMinPercentage()) {
                eligible = false;
                mismatchReasons.add("Academic score (" + candidate.getPercentage() + "%) is below the minimum threshold (" + opp.getMinPercentage() + "%).");
            } else {
                matchReasons.add("Academic percentage (" + candidate.getPercentage() + "%) satisfies the minimum requirement of " + opp.getMinPercentage() + "%.");
            }
        }

        // 3. Qualification check
        if (candidate.getQualification() != null && !candidate.getQualification().trim().isEmpty()) {
            String qual = candidate.getQualification().toLowerCase();
            String req = (opp.getQualification() + " " + opp.getField()).toLowerCase();

            if (req.contains("any discipline") || req.contains("graduate") || req.contains(qual)) {
                matchReasons.add("Your qualification matches the target discipline requirement.");
            } else {
                matchReasons.add("Qualification will be reviewed based on official notification equivalency guidelines.");
            }
        }

        return new EligibilityCheckDto.Result(opp.getId(), opp.getTitle(), eligible, matchReasons, mismatchReasons);
    }
}
