package com.tvarah.repository;

import com.tvarah.model.entity.CandidateJobInterview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateJobInterviewRepository extends JpaRepository<CandidateJobInterview, UUID> {

    List<CandidateJobInterview> findByCandidateJobIdInOrderByRoundNumberAsc(List<UUID> candidateJobIds);
}
