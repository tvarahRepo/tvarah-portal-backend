package com.tvarah.repository;

import com.tvarah.model.entity.CandidateJobEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateJobEvaluationRepository extends JpaRepository<CandidateJobEvaluation, UUID> {

    List<CandidateJobEvaluation> findByCandidateJobIdIn(List<UUID> candidateJobIds);
}
