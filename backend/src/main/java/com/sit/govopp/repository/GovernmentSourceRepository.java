package com.sit.govopp.repository;

import com.sit.govopp.entity.GovernmentSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GovernmentSourceRepository extends JpaRepository<GovernmentSource, Long> {
    Optional<GovernmentSource> findByAgencyCode(String agencyCode);
    List<GovernmentSource> findByEnabledTrue();
}
