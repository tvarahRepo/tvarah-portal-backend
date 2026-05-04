package com.tvarah.service.impl;

import com.tvarah.exception.ResourceNotFoundException;
import com.tvarah.model.entity.Company;
import com.tvarah.model.request.CompanyRequest;
import com.tvarah.model.response.CompanyResponse;
import com.tvarah.repository.CompanyRepository;
import com.tvarah.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .nameAlias(request.getNameAlias())
                .type(request.getType())
                .industry(request.getIndustry())
                .headquarterCity(request.getHeadquarterCity())
                .headquarterState(request.getHeadquarterState())
                .headquarterCountry(request.getHeadquarterCountry())
                .foundedYear(request.getFoundedYear())
                .noOfEmployees(request.getNoOfEmployees())
                .avgRating(request.getAvgRating())
                .mcaVerified(request.getMcaVerified() != null ? request.getMcaVerified() : false)
                .status(request.getStatus())
                .sector(request.getSector())
                .companyDomain(request.getCompanyDomain())
                .candidatePersonalDomain(request.getCandidatePersonalDomain())
                .domainSource(request.getDomainSource())
                .build();

        return toResponse(companyRepository.save(company));
    }

    @Override
    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CompanyResponse findById(UUID id) {
        return companyRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + id));
    }

    private CompanyResponse toResponse(Company c) {
        return CompanyResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .nameAlias(c.getNameAlias())
                .type(c.getType())
                .industry(c.getIndustry())
                .headquarterCity(c.getHeadquarterCity())
                .headquarterState(c.getHeadquarterState())
                .headquarterCountry(c.getHeadquarterCountry())
                .foundedYear(c.getFoundedYear())
                .noOfEmployees(c.getNoOfEmployees())
                .reviewCount(c.getReviewCount())
                .avgRating(c.getAvgRating())
                .mcaVerified(c.getMcaVerified())
                .status(c.getStatus())
                .sector(c.getSector())
                .companyDomain(c.getCompanyDomain())
                .candidatePersonalDomain(c.getCandidatePersonalDomain())
                .domainSource(c.getDomainSource())
                .build();
    }
}
