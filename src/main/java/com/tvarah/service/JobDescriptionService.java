package com.tvarah.service;

import com.tvarah.model.response.JobDescriptionResponse;

import java.util.List;

public interface JobDescriptionService {

    List<JobDescriptionResponse> findAll();
}
