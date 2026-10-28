package com.sit.govopp.service;

import com.sit.govopp.entity.ApplicationBookmark;
import com.sit.govopp.entity.Opportunity;
import com.sit.govopp.repository.ApplicationBookmarkRepository;
import com.sit.govopp.repository.OpportunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationTrackerService {

    private final ApplicationBookmarkRepository bookmarkRepository;
    private final OpportunityRepository opportunityRepository;

    public ApplicationTrackerService(ApplicationBookmarkRepository bookmarkRepository,
                                     OpportunityRepository opportunityRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public List<ApplicationBookmark> getBookmarksForUser(String userIdentifier) {
        String user = (userIdentifier == null || userIdentifier.trim().isEmpty()) ? "student@sit.ac.in" : userIdentifier;
        return bookmarkRepository.findByUserIdentifierOrderByCreatedAtDesc(user);
    }

    @Transactional
    public ApplicationBookmark bookmarkOpportunity(Long opportunityId, String userIdentifier, String notes) {
        String user = (userIdentifier == null || userIdentifier.trim().isEmpty()) ? "student@sit.ac.in" : userIdentifier;
        Opportunity opp = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity not found with id: " + opportunityId));

        Optional<ApplicationBookmark> existing = bookmarkRepository.findByUserIdentifierAndOpportunityId(user, opportunityId);
        if (existing.isPresent()) {
            ApplicationBookmark b = existing.get();
            if (notes != null) b.setNotes(notes);
            return bookmarkRepository.save(b);
        }

        ApplicationBookmark bookmark = new ApplicationBookmark(opp, user, "BOOKMARKED", notes);
        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public ApplicationBookmark updateBookmarkStatus(Long bookmarkId, String newStatus, String notes) {
        ApplicationBookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new IllegalArgumentException("Bookmark not found with id: " + bookmarkId));

        if (newStatus != null && !newStatus.trim().isEmpty()) {
            bookmark.setStatus(newStatus.toUpperCase());
        }
        if (notes != null) {
            bookmark.setNotes(notes);
        }
        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long opportunityId, String userIdentifier) {
        String user = (userIdentifier == null || userIdentifier.trim().isEmpty()) ? "student@sit.ac.in" : userIdentifier;
        bookmarkRepository.deleteByUserIdentifierAndOpportunityId(user, opportunityId);
    }
}
