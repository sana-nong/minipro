package com.sit.govopp.repository;

import com.sit.govopp.entity.ApplicationBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationBookmarkRepository extends JpaRepository<ApplicationBookmark, Long> {
    List<ApplicationBookmark> findByUserIdentifierOrderByCreatedAtDesc(String userIdentifier);
    Optional<ApplicationBookmark> findByUserIdentifierAndOpportunityId(String userIdentifier, Long opportunityId);
    boolean existsByUserIdentifierAndOpportunityId(String userIdentifier, Long opportunityId);
    void deleteByUserIdentifierAndOpportunityId(String userIdentifier, Long opportunityId);
}
