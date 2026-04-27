package com.tvarah.service.impl;

import com.tvarah.mapper.CandidateMapper;
import com.tvarah.model.entity.*;
import com.tvarah.model.response.CandidateResponse;
import com.tvarah.repository.*;
import com.tvarah.service.CandidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobTitleRepository jobTitleRepository;
    private final DesignationRepository designationRepository;
    private final CompanyRepository companyRepository;
    private final CandidatePreferenceRepository candidatePreferenceRepository;
    private final CandidateNoticePeriodRepository candidateNoticePeriodRepository;
    private final CandidateScoreRepository candidateScoreRepository;
    private final CandidateMapper candidateMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CandidateResponse> findAll() {
        log.debug("Fetching all candidates");

        List<Candidate> candidates = candidateRepository.findAll();
        if (candidates.isEmpty()) return List.of();

        List<UUID> candidateIds = candidates.stream().map(Candidate::getId).collect(Collectors.toList());

        // Collect FK IDs for lookup tables
        Set<UUID> jobTitleIds = candidates.stream()
                .map(Candidate::getJobTitleId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<UUID> designationIds = candidates.stream()
                .map(Candidate::getDesignationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<UUID> companyIds = candidates.stream()
                .map(Candidate::getCurrentCompanyId).filter(Objects::nonNull).collect(Collectors.toSet());

        // Batch fetch — one query per table
        Map<UUID, String> jobTitleNames = jobTitleRepository.findAllById(jobTitleIds).stream()
                .collect(Collectors.toMap(JobTitle::getId, JobTitle::getName));

        Map<UUID, String> designationNames = designationRepository.findAllById(designationIds).stream()
                .collect(Collectors.toMap(Designation::getId, Designation::getName));

        Map<UUID, String> companyNames = companyRepository.findAllById(companyIds).stream()
                .collect(Collectors.toMap(Company::getId, Company::getName));

        Map<UUID, CandidatePreference> preferenceMap = candidatePreferenceRepository
                .findByCandidateIdIn(candidateIds).stream()
                .collect(Collectors.toMap(CandidatePreference::getCandidateId, p -> p));

        Map<UUID, CandidateNoticePeriod> noticePeriodMap = candidateNoticePeriodRepository
                .findByCandidateIdIn(candidateIds).stream()
                .collect(Collectors.toMap(CandidateNoticePeriod::getCandidateId, n -> n));

        Map<UUID, CandidateScore> scoreMap = candidateScoreRepository
                .findByCandidateIdIn(candidateIds).stream()
                .collect(Collectors.toMap(CandidateScore::getCandidateId, s -> s));

        log.debug("Returning {} candidates", candidates.size());
        return candidateMapper.toResponseList(
                candidates, jobTitleNames, designationNames, companyNames,
                preferenceMap, noticePeriodMap, scoreMap);
    }
}
