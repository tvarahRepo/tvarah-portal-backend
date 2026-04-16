package com.tvarah.service.impl;

import com.tvarah.mapper.JobDescriptionMapper;
import com.tvarah.model.entity.JobDescription;
import com.tvarah.model.response.JobDescriptionResponse;
import com.tvarah.repository.CompanyRepository;
import com.tvarah.repository.JobDescriptionRepository;
import com.tvarah.repository.JobTitleRepository;
import com.tvarah.repository.SkillRepository;
import com.tvarah.service.JobDescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private final JobDescriptionRepository jobDescriptionRepository;
    private final CompanyRepository companyRepository;
    private final JobTitleRepository jobTitleRepository;
    private final SkillRepository skillRepository;
    private final JobDescriptionMapper jobDescriptionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<JobDescriptionResponse> findAll() {
        log.debug("Fetching all job descriptions");
        List<JobDescription> jds = jobDescriptionRepository.findAll();
        if (jds.isEmpty()) return List.of();

        // Collect IDs needed for lookup
        Set<UUID> companyIds = jds.stream()
                .map(JobDescription::getCompanyId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<UUID> jobTitleIds = jds.stream()
                .map(JobDescription::getJobTitleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<UUID> skillIds = jds.stream()
                .flatMap(jd -> Stream.concat(
                        jd.getRequiredSkills() != null ? jd.getRequiredSkills().stream() : Stream.empty(),
                        jd.getGoodToHaveSkills() != null ? jd.getGoodToHaveSkills().stream() : Stream.empty()))
                .collect(Collectors.toSet());

        // Batch fetch — one query each
        Map<UUID, String> companyNames = companyRepository.findAllById(companyIds).stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName()));

        Map<UUID, String> jobTitleNames = jobTitleRepository.findAllById(jobTitleIds).stream()
                .collect(Collectors.toMap(jt -> jt.getId(), jt -> jt.getName()));

        Map<UUID, String> skillNames = skillRepository.findAllById(skillIds).stream()
                .collect(Collectors.toMap(s -> s.getId(), s -> s.getName()));

        return jobDescriptionMapper.toResponseList(jds, companyNames, jobTitleNames, skillNames);
    }
}
