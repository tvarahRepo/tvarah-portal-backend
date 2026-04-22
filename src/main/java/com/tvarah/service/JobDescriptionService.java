package com.tvarah.service;

import com.tvarah.model.response.JdCandidateResponse;
import com.tvarah.model.response.JobDescriptionResponse;

import java.util.List;
import java.util.UUID;

public interface JobDescriptionService {

    List<JobDescriptionResponse> findAll();

    List<JdCandidateResponse> getCandidatesForJd(UUID jdId);

    void cancelJobDescription(UUID id);
}
