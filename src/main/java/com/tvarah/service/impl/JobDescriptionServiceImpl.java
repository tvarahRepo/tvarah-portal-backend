package com.tvarah.service.impl;

import com.tvarah.exception.ResourceNotFoundException;
import com.tvarah.mapper.JobDescriptionMapper;
import com.tvarah.model.entity.*;
import com.tvarah.model.response.*;
import com.tvarah.repository.*;
import com.tvarah.service.JobDescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final CandidateJobRepository candidateJobRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateJobEvaluationRepository candidateJobEvaluationRepository;
    private final CandidateJobInterviewRepository candidateJobInterviewRepository;
    private final CandidateScoreRepository candidateScoreRepository;
    private final DesignationRepository designationRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<JdCandidateResponse> getCandidatesForJd(UUID jdId) {
        log.debug("Fetching candidates for JD: {}", jdId);

        List<CandidateJob> candidateJobs = candidateJobRepository.findByJobId(jdId);
        if (candidateJobs.isEmpty()) return List.of();

        List<UUID> candidateJobIds = candidateJobs.stream().map(CandidateJob::getId).collect(Collectors.toList());
        List<UUID> candidateIds = candidateJobs.stream().map(CandidateJob::getCandidateId).collect(Collectors.toList());

        Map<UUID, Candidate> candidateMap = candidateRepository.findAllById(candidateIds).stream()
                .collect(Collectors.toMap(Candidate::getId, c -> c));

        Map<UUID, CandidateJobEvaluation> evaluationMap = candidateJobEvaluationRepository
                .findByCandidateJobIdIn(candidateJobIds).stream()
                .collect(Collectors.toMap(CandidateJobEvaluation::getCandidateJobId, e -> e));

        Map<UUID, List<CandidateJobInterview>> interviewMap = candidateJobInterviewRepository
                .findByCandidateJobIdInOrderByRoundNumberAsc(candidateJobIds).stream()
                .collect(Collectors.groupingBy(CandidateJobInterview::getCandidateJobId));

        Map<UUID, CandidateScore> scoreMap = candidateScoreRepository
                .findByCandidateIdIn(candidateIds).stream()
                .collect(Collectors.toMap(CandidateScore::getCandidateId, s -> s));

        Set<UUID> designationIds = candidateMap.values().stream()
                .map(Candidate::getDesignationId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<UUID> companyIds = candidateMap.values().stream()
                .map(Candidate::getCurrentCompanyId).filter(Objects::nonNull).collect(Collectors.toSet());

        Map<UUID, String> designationNames = designationRepository.findAllById(designationIds).stream()
                .collect(Collectors.toMap(Designation::getId, Designation::getName));
        Map<UUID, String> companyNames = companyRepository.findAllById(companyIds).stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName()));

        return candidateJobs.stream()
                .map(cj -> buildResponse(
                        candidateMap.get(cj.getCandidateId()),
                        evaluationMap.get(cj.getId()),
                        interviewMap.getOrDefault(cj.getId(), List.of()),
                        scoreMap.get(cj.getCandidateId()),
                        designationNames,
                        companyNames))
                .sorted(Comparator.comparing(
                        r -> r.getEvaluation() != null && r.getEvaluation().getJdOverallMatchScore() != null
                                ? r.getEvaluation().getJdOverallMatchScore()
                                : BigDecimal.ZERO,
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelJobDescription(UUID id) {
        log.debug("Cancelling job description: {}", id);
        JobDescription jd = jobDescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job description not found: " + id));
        jd.setStatus("Cancelled");
        jobDescriptionRepository.save(jd);
        log.info("Job description {} marked as Cancelled", id);
    }

    private JdCandidateResponse buildResponse(Candidate candidate, CandidateJobEvaluation evaluation,
                                               List<CandidateJobInterview> interviews, CandidateScore score,
                                               Map<UUID, String> designationNames, Map<UUID, String> companyNames) {
        CandidateEvaluationResponse evalResponse = evaluation == null ? null : CandidateEvaluationResponse.builder()
                .id(evaluation.getId())
                .jdOverallMatchScore(evaluation.getJdOverallMatchScore())
                .skillMatchScore(evaluation.getSkillMatchScore())
                .domainMatchScore(evaluation.getDomainMatchScore())
                .jdExpRelevanceScore(evaluation.getJdExpRelevanceScore())
                .score(evaluation.getScore())
                .maxScore(evaluation.getMaxScore())
                .status(evaluation.getStatus())
                .build();

        List<CandidateInterviewResponse> interviewResponses = interviews.stream()
                .map(i -> CandidateInterviewResponse.builder()
                        .id(i.getId())
                        .roundNumber(i.getRoundNumber())
                        .interviewerName(i.getInterviewerName())
                        .interviewerEmail(i.getInterviewerEmail())
                        .interviewerType(i.getInterviewerType())
                        .scheduledOn(i.getScheduledOn())
                        .mode(i.getMode())
                        .status(i.getStatus())
                        .questions(i.getQuestions())
                        .focusAreas(i.getFocusAreas())
                        .feedback(i.getFeedback())
                        .score(i.getScore())
                        .maxScore(i.getMaxScore())
                        .build())
                .collect(Collectors.toList());

        return JdCandidateResponse.builder()
                .candidateId(candidate != null ? candidate.getId() : null)
                .firstName(candidate != null ? candidate.getFirstName() : null)
                .lastName(candidate != null ? candidate.getLastName() : null)
                .primaryEmail(candidate != null ? candidate.getPrimaryEmail() : null)
                .primaryPhoneNumber(candidate != null ? candidate.getPrimaryPhoneNumber() : null)
                .city(candidate != null ? candidate.getCity() : null)
                .country(candidate != null ? candidate.getCountry() : null)
                .status(candidate != null ? candidate.getStatus() : null)
                .designation(candidate != null ? designationNames.get(candidate.getDesignationId()) : null)
                .currentCompany(candidate != null ? companyNames.get(candidate.getCurrentCompanyId()) : null)
                .totalExperience(candidate != null ? candidate.getTotalExperience() : null)
                .resumeScore(score != null ? score.getScore() : null)
                .evaluation(evalResponse)
                .interviews(interviewResponses)
                .build();
    }
}
