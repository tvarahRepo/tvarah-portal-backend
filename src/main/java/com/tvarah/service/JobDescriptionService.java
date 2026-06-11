package com.tvarah.service;

import com.tvarah.model.request.JdConfigRequest;
import com.tvarah.model.request.JdEnrichRequest;
import com.tvarah.model.request.JdScorecardRequest;
import com.tvarah.model.response.JdCandidateResponse;
import com.tvarah.model.response.JobDescriptionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface JobDescriptionService {

    List<JobDescriptionResponse> findAll();

    List<JdCandidateResponse> getCandidatesForJd(UUID jdId);

    void cancelJobDescription(UUID id);

    JobDescriptionResponse create(MultipartFile file, UUID companyId, Integer totalPositions, Integer totalRounds, UUID assignedToUserId, String status);

    void enrich(UUID id, JdEnrichRequest request);

    void configure(UUID id, JdConfigRequest request);

    void updateScorecard(UUID id, JdScorecardRequest request);
}
