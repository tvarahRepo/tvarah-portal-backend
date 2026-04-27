package com.tvarah.repository;

import com.tvarah.model.entity.CandidateNoticePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateNoticePeriodRepository extends JpaRepository<CandidateNoticePeriod, UUID> {

    List<CandidateNoticePeriod> findByCandidateIdIn(List<UUID> candidateIds);
}
