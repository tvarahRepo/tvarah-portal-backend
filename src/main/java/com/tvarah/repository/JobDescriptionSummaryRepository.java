package com.tvarah.repository;

import com.tvarah.model.entity.JobDescriptionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobDescriptionSummaryRepository extends JpaRepository<JobDescriptionSummary, UUID> {

    Optional<JobDescriptionSummary> findByJobDescriptionId(UUID jobDescriptionId);
}
