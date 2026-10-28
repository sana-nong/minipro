package com.sit.govopp.repository;

import com.sit.govopp.entity.Opportunity;
import com.sit.govopp.entity.OpportunityCategory;
import com.sit.govopp.entity.OpportunityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long>, JpaSpecificationExecutor<Opportunity> {

    Optional<Opportunity> findByUniqueSourceId(String uniqueSourceId);

    boolean existsByUniqueSourceId(String uniqueSourceId);

    List<Opportunity> findByCategory(OpportunityCategory category);

    List<Opportunity> findByStatus(OpportunityStatus status);

    @Query("SELECT o FROM Opportunity o WHERE o.deadline BETWEEN :startDate AND :endDate ORDER BY o.deadline ASC")
    List<Opportunity> findExpiringBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o.category, COUNT(o) FROM Opportunity o GROUP BY o.category")
    List<Object[]> countGroupByCategory();

    @Query("SELECT o.status, COUNT(o) FROM Opportunity o GROUP BY o.status")
    List<Object[]> countGroupByStatus();

    long countByStatus(OpportunityStatus status);

    @Query("SELECT DISTINCT o.organization FROM Opportunity o ORDER BY o.organization ASC")
    List<String> findDistinctOrganizations();

    @Query("SELECT DISTINCT o.location FROM Opportunity o ORDER BY o.location ASC")
    List<String> findDistinctLocations();
}
