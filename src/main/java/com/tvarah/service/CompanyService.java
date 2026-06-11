package com.tvarah.service;

import com.tvarah.model.request.CompanyRequest;
import com.tvarah.model.response.CompanyResponse;
import com.tvarah.model.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CompanyService {

    CompanyResponse create(CompanyRequest request);

    List<CompanyResponse> findAll();

    List<CompanyResponse> findByCurrentUser();

    CompanyResponse findById(UUID id);

    CompanyResponse uploadLogo(UUID id, MultipartFile logo);

    byte[] getLogo(UUID id);

    String getLogoContentType(UUID id);

    List<UserResponse> findUsersByCompanyId(UUID companyId);
}
