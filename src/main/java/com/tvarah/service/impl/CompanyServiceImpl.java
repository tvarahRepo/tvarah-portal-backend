package com.tvarah.service.impl;

import com.tvarah.exception.BadRequestException;
import com.tvarah.exception.ResourceNotFoundException;
import com.tvarah.model.entity.Company;
import com.tvarah.model.entity.CompanyUser;
import com.tvarah.model.entity.User;
import com.tvarah.model.enums.UserStatus;
import com.tvarah.model.request.CompanyRequest;
import com.tvarah.model.response.CompanyResponse;
import com.tvarah.model.response.UserResponse;
import com.tvarah.repository.CompanyRepository;
import com.tvarah.repository.CompanyUserRepository;
import com.tvarah.repository.UserRepository;
import com.tvarah.security.SecurityUtils;
import com.tvarah.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .reviewCount(0)
                .mcaVerified(request.getMcaVerified() != null ? request.getMcaVerified() : false)
                .nameAlias(request.getNameAlias())
                .type(request.getType())
                .industry(request.getIndustry())
                .headquarterCity(request.getHeadquarterCity())
                .headquarterState(request.getHeadquarterState())
                .headquarterCountry(request.getHeadquarterCountry())
                .foundedYear(request.getFoundedYear())
                .noOfEmployees(request.getNoOfEmployees())
                .avgRating(request.getAvgRating())
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
    public List<CompanyResponse> findByCurrentUser() {
        if (SecurityUtils.hasRole("SITE_ADMIN")) {
            return companyRepository.findAll().stream()
                    .map(company -> {
                        User contact = companyUserRepository.findByCompanyId(company.getId()).stream()
                                .map(cu -> userRepository.findById(cu.getUserId()).orElse(null))
                                .filter(u -> u != null && u.getStatus() == UserStatus.DRAFT)
                                .findFirst().orElse(null);
                        return toResponseWithContact(company, contact);
                    })
                    .toList();
        }
        String keycloakUserId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + keycloakUserId));
        List<UUID> companyIds = companyUserRepository.findByUserId(user.getId()).stream()
                .map(CompanyUser::getCompanyId)
                .toList();
        return companyRepository.findAllById(companyIds).stream()
                .map(company -> {
                    User contact = companyUserRepository.findByCompanyId(company.getId()).stream()
                            .map(cu -> userRepository.findById(cu.getUserId()).orElse(null))
                            .filter(u -> u != null && u.getStatus() == UserStatus.DRAFT)
                            .findFirst().orElse(null);
                    return toResponseWithContact(company, contact);
                })
                .toList();
    }

    @Override
    public CompanyResponse findById(UUID id) {
        return companyRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + id));
    }

    @Override
    @Transactional
    public CompanyResponse uploadLogo(UUID id, MultipartFile logo) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + id));
        try {
            company.setLogo(logo.getBytes());
            company.setLogoContentType(logo.getContentType());
        } catch (Exception e) {
            log.error("Failed to read logo file for company: {}", id, e);
            throw new BadRequestException("Failed to process logo file");
        }
        return toResponse(companyRepository.save(company));
    }

    @Override
    public byte[] getLogo(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + id));
        if (company.getLogo() == null) {
            throw new ResourceNotFoundException("No logo found for company: " + id);
        }
        return company.getLogo();
    }

    @Override
    public String getLogoContentType(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + id));
        if (company.getLogoContentType() == null) {
            throw new ResourceNotFoundException("No logo found for company: " + id);
        }
        return company.getLogoContentType();
    }

    @Override
    public List<UserResponse> findUsersByCompanyId(UUID companyId) {
        return companyUserRepository.findByCompanyId(companyId).stream()
                .map(cu -> userRepository.findById(cu.getUserId()).orElse(null))
                .filter(u -> u != null)
                .map(u -> UserResponse.builder()
                        .id(u.getId().toString())
                        .keycloakUserId(u.getKeycloakUserId())
                        .email(u.getEmail())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .phoneNumber(u.getPhoneNumber())
                        .role(u.getRole())
                        .status(u.getStatus())
                        .build())
                .toList();
    }

    private CompanyResponse toResponse(Company c) {
        return toResponseWithContact(c, null);
    }

    private CompanyResponse toResponseWithContact(Company c, User contact) {
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
                .logoUrl(c.getLogo() != null ? baseUrl + "/companies/" + c.getId() + "/logo" : null)
                .contactFirstName(contact != null ? contact.getFirstName() : null)
                .contactLastName(contact != null ? contact.getLastName() : null)
                .contactEmail(contact != null ? contact.getEmail() : null)
                .contactPhone(contact != null ? contact.getPhoneNumber() : null)
                .contactRole(contact != null ? contact.getRole() : null)
                .build();
    }
}
