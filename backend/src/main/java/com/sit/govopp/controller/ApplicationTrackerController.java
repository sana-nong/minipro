package com.sit.govopp.controller;

import com.sit.govopp.entity.ApplicationBookmark;
import com.sit.govopp.service.ApplicationTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tracker")
public class ApplicationTrackerController {

    private final ApplicationTrackerService trackerService;

    public ApplicationTrackerController(ApplicationTrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<ApplicationBookmark>> getBookmarks(
            @RequestParam(defaultValue = "student@sit.ac.in") String userIdentifier) {
        return ResponseEntity.ok(trackerService.getBookmarksForUser(userIdentifier));
    }

    @PostMapping("/bookmark")
    public ResponseEntity<ApplicationBookmark> addBookmark(@RequestBody Map<String, Object> payload) {
        Long opportunityId = Long.valueOf(payload.get("opportunityId").toString());
        String userIdentifier = payload.containsKey("userIdentifier") ? (String) payload.get("userIdentifier") : "student@sit.ac.in";
        String notes = payload.containsKey("notes") ? (String) payload.get("notes") : "";

        ApplicationBookmark bookmark = trackerService.bookmarkOpportunity(opportunityId, userIdentifier, notes);
        return ResponseEntity.ok(bookmark);
    }

    @PutMapping("/bookmark/{id}")
    public ResponseEntity<ApplicationBookmark> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        String notes = payload.get("notes");
        return ResponseEntity.ok(trackerService.updateBookmarkStatus(id, status, notes));
    }

    @DeleteMapping("/bookmark/{opportunityId}")
    public ResponseEntity<Void> removeBookmark(
            @PathVariable Long opportunityId,
            @RequestParam(defaultValue = "student@sit.ac.in") String userIdentifier) {
        trackerService.removeBookmark(opportunityId, userIdentifier);
        return ResponseEntity.noContent().build();
    }
}
