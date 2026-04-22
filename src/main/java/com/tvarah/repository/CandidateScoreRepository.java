package com.tvarah.repository;

import com.tvarah.model.entity.CandidateScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateScoreRepository extends JpaRepository<CandidateScore, UUID> {

    List<CandidateScore> findByCandidateIdIn(List<UUID> candidateIds);
}
