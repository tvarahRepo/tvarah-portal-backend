package com.tvarah.repository;

import com.tvarah.model.entity.CandidateJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateJobRepository extends JpaRepository<CandidateJob, UUID> {

    List<CandidateJob> findByJobId(UUID jobId);
}
