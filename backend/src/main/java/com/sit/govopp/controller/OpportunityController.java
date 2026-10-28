package com.sit.govopp.controller;

import com.sit.govopp.dto.EligibilityCheckDto;
import com.sit.govopp.dto.OpportunityFilterDto;
import com.sit.govopp.dto.StatsSummaryDto;
import com.sit.govopp.entity.Opportunity;
import com.sit.govopp.entity.OpportunityCategory;
import com.sit.govopp.entity.OpportunityStatus;
import com.sit.govopp.repository.OpportunityRepository;
import com.sit.govopp.service.SearchFilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final SearchFilterService searchFilterService;
    private final OpportunityRepository opportunityRepository;

    public OpportunityController(SearchFilterService searchFilterService,
                                 OpportunityRepository opportunityRepository) {
        this.searchFilterService = searchFilterService;
        this.opportunityRepository = opportunityRepository;
    }

    /**
     * Search and Filter endpoint (Objective 2)
     */
    @GetMapping
    public ResponseEntity<List<Opportunity>> getOpportunities(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) OpportunityCategory category,
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) OpportunityStatus status,
            @RequestParam(required = false) Integer closingInDays,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Double userPercentage,
            @RequestParam(defaultValue = "deadline") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        OpportunityFilterDto filter = new OpportunityFilterDto();
        filter.setQuery(query);
        filter.setCategory(category);
        filter.setQualification(qualification);
        filter.setLocation(location);
        filter.setStatus(status);
        filter.setClosingInDays(closingInDays);
        filter.setMaxAge(maxAge);
        filter.setUserPercentage(userPercentage);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        List<Opportunity> results = searchFilterService.searchAndFilter(filter);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opportunity> getOpportunityById(@PathVariable Long id) {
        return opportunityRepository.findById(id)
                .map(opp -> {
                    opp.refreshStatus();
                    return ResponseEntity.ok(opp);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsSummaryDto> getStats() {
        return ResponseEntity.ok(searchFilterService.getDashboardStats());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, String>>> getCategories() {
        List<Map<String, String>> categories = Arrays.stream(OpportunityCategory.values())
                .map(c -> Map.of("key", c.name(), "label", c.getDisplayName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<String>> getOrganizations() {
        return ResponseEntity.ok(opportunityRepository.findDistinctOrganizations());
    }

    @GetMapping("/locations")
    public ResponseEntity<List<String>> getLocations() {
        return ResponseEntity.ok(opportunityRepository.findDistinctLocations());
    }

    @PostMapping("/{id}/check-eligibility")
    public ResponseEntity<EligibilityCheckDto.Result> checkEligibility(
            @PathVariable Long id,
            @RequestBody EligibilityCheckDto candidateProfile) {
        EligibilityCheckDto.Result result = searchFilterService.evaluateEligibility(id, candidateProfile);
        return ResponseEntity.ok(result);
    }
}
