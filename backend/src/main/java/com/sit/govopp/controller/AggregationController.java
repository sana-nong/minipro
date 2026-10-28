package com.sit.govopp.controller;

import com.sit.govopp.dto.SyncResultDto;
import com.sit.govopp.entity.GovernmentSource;
import com.sit.govopp.repository.GovernmentSourceRepository;
import com.sit.govopp.service.DataAggregationEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aggregation")
public class AggregationController {

    private final DataAggregationEngine aggregationEngine;
    private final GovernmentSourceRepository sourceRepository;

    public AggregationController(DataAggregationEngine aggregationEngine,
                                 GovernmentSourceRepository sourceRepository) {
        this.aggregationEngine = aggregationEngine;
        this.sourceRepository = sourceRepository;
    }

    @GetMapping("/sources")
    public ResponseEntity<List<GovernmentSource>> getAllSources() {
        return ResponseEntity.ok(sourceRepository.findAll());
    }

    /**
     * Trigger full crawler aggregation across all government sources (Objective 1 Demo Button)
     */
    @PostMapping("/sync-all")
    public ResponseEntity<List<SyncResultDto>> syncAllSources() {
        List<SyncResultDto> results = aggregationEngine.syncAllSources();
        return ResponseEntity.ok(results);
    }

    /**
     * Trigger crawler for a specific portal (e.g. UPSC or AICTE)
     */
    @PostMapping("/sync/{sourceId}")
    public ResponseEntity<SyncResultDto> syncSingleSource(@PathVariable Long sourceId) {
        SyncResultDto result = aggregationEngine.syncSourceById(sourceId);
        return ResponseEntity.ok(result);
    }
}
