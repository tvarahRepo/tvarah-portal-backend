package com.tvarah.service;

import com.tvarah.model.request.CompanyRequest;
import com.tvarah.model.response.CompanyResponse;

import java.util.List;
import java.util.UUID;

public interface CompanyService {

    CompanyResponse create(CompanyRequest request);

    List<CompanyResponse> findAll();

    CompanyResponse findById(UUID id);
}
