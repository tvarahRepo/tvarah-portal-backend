package com.tvarah.service;

import com.tvarah.model.response.CandidateResponse;

import java.util.List;

public interface CandidateService {

    List<CandidateResponse> findAll();
}
