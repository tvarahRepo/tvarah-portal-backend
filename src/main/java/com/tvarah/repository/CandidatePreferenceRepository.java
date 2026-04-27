package com.tvarah.repository;

import com.tvarah.model.entity.CandidatePreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidatePreferenceRepository extends JpaRepository<CandidatePreference, UUID> {

    List<CandidatePreference> findByCandidateIdIn(List<UUID> candidateIds);
}
