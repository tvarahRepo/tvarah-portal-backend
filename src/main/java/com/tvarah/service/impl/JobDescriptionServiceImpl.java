package com.tvarah.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvarah.exception.BadRequestException;
import com.tvarah.exception.JdMissingFieldsException;
import com.tvarah.exception.ResourceNotFoundException;
import com.tvarah.mapper.JobDescriptionMapper;
import com.tvarah.model.entity.*;
import com.tvarah.model.ml.MlJdData;
import com.tvarah.model.ml.MlJdParseResponse;
import com.tvarah.model.ml.MlLocation;
import com.tvarah.model.ml.MlMandatorySkills;
import com.tvarah.model.request.JdConfigRequest;
import com.tvarah.model.request.JdEnrichRequest;
import com.tvarah.model.request.JdScorecardRequest;
import com.tvarah.model.response.*;
import com.tvarah.repository.*;
import com.tvarah.security.SecurityUtils;
import com.tvarah.service.JobDescriptionService;
import com.tvarah.service.MlJdParseClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private final JobDescriptionRepository jobDescriptionRepository;
    private final JobDescriptionSummaryRepository jobDescriptionSummaryRepository;
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
    private final MlJdParseClient mlJdParseClient;
    private final ObjectMapper objectMapper;

    // ── Create (parse → validate → save) ──────────────────────────────────────

    @Override
    @Transactional
    public JobDescriptionResponse create(MultipartFile file, UUID companyId,
                                         Integer totalPositions, Integer totalRounds) {
        if (companyId == null) {
            throw new BadRequestException("companyId is required");
        }
        log.info("Creating JD from file: {}", file.getOriginalFilename());

        // 1. Call ML
        MlJdParseResponse mlResponse = mlJdParseClient.parse(file);
        MlJdData jdData = mlResponse.getJdData();

        // 2. Validate ML response against DB schema required fields
        validateRequiredFields(jdData);

        // 3. Map ML → entity fields
        MlMandatorySkills ms = jdData.getMandatorySkills();

        String locationCity = null;
        String locationCountry = null;
        com.fasterxml.jackson.databind.JsonNode locationsJson = null;
        if (jdData.getLocation() != null && !jdData.getLocation().isEmpty()) {
            MlLocation first = jdData.getLocation().stream()
                    .filter(l -> l.getCity() != null || l.getCountry() != null)
                    .findFirst().orElse(jdData.getLocation().get(0));
            locationCity = first.getCity();
            locationCountry = first.getCountry();
            locationsJson = objectMapper.valueToTree(jdData.getLocation());
        }

        UUID jobTitleId = resolveJobTitle(jdData.getRoleTitle(), jdData.getJobLevel());

        List<UUID> programmingLangIds = resolveSkillIds(safeList(ms != null ? ms.getProgrammingLanguages() : null), "Programming Language", true);
        List<UUID> frameworkIds       = resolveSkillIds(safeList(ms != null ? ms.getFrameworksAndLibraries() : null), "Framework/Library",  true);
        List<UUID> toolIds            = resolveSkillIds(safeList(ms != null ? ms.getTools() : null),                  "Tool",               true);
        List<UUID> dbIds              = resolveSkillIds(safeList(ms != null ? ms.getDatabases() : null),              "Database",           true);
        List<UUID> cloudIds           = resolveSkillIds(safeList(ms != null ? ms.getCloudAndInfra() : null),          "Cloud/Infrastructure", true);
        List<UUID> optionalSkillIds   = resolveSkillIds(safeList(jdData.getOptionalSkills()),                         "General",            true);

        List<UUID> requiredSkills = Stream.of(programmingLangIds, frameworkIds, toolIds, dbIds, cloudIds)
                .flatMap(Collection::stream).distinct().collect(Collectors.toList());

        // 4. Persist
        String currentUser = SecurityUtils.getCurrentEmail().orElse("system");
        Instant now = Instant.now();

        JobDescription jd = new JobDescription();
        jd.setCode(generateCode());
        jd.setCompanyId(companyId);
        jd.setJobTitleId(jobTitleId);
        jd.setJobType(jdData.getJobType());
        jd.setJobMode(normalizeWorkMode(jdData.getWorkMode()));
        jd.setJobLevel(normalizeJobLevel(jdData.getJobLevel()));
        jd.setJobDescriptionText(buildDescriptionText(jdData.getSummaryResponsibilities(), jdData.getRoleTitle()));
        jd.setSummaryResponsibilities(jdData.getSummaryResponsibilities());
        jd.setExperienceMinYrs(jdData.getMinYearsExperience());
        jd.setExperienceMaxYrs(jdData.getMaxYearsExperience());
        jd.setRequiredSkills(requiredSkills);
        jd.setGoodToHaveSkills(optionalSkillIds);
        jd.setTotalPositions(totalPositions != null ? totalPositions : 2);
        jd.setTotalPositionsSelected(0);
        jd.setTotalRounds(totalRounds != null ? totalRounds : 2);
        jd.setStatus("Draft");
        jd.setCreatedOn(now);
        jd.setUpdatedOn(now);
        jd.setCreatedBy(currentUser);
        jd.setUpdatedBy(currentUser);
        jd.setLocationCity(locationCity);
        jd.setLocationCountry(locationCountry);
        jd.setLocations(locationsJson);
        jd.setDegreeRequired(jdData.getDegreeRequired());
        jd.setSkillsProgrammingLanguages(programmingLangIds);
        jd.setSkillsFrameworksLibraries(frameworkIds);
        jd.setSkillsTools(toolIds);
        jd.setSkillsDatabases(dbIds);
        jd.setSkillsCloudInfra(cloudIds);

        jd = jobDescriptionRepository.save(jd);
        log.info("JobDescription saved with id: {}, code: {}", jd.getId(), jd.getCode());

        JobDescriptionSummary summary = new JobDescriptionSummary();
        summary.setJobDescriptionId(jd.getId());
        summary.setParsedJd(objectMapper.valueToTree(mlResponse));
        summary.setMlVerdict(mlResponse.getVerdict());
        summary.setJudgeResults(objectMapper.valueToTree(mlResponse.getJudgeResults()));
        summary.setMlReflectionLoop(mlResponse.getReflectionLoop() != null
                ? mlResponse.getReflectionLoop().shortValue() : null);
        jobDescriptionSummaryRepository.save(summary);

        return buildCreateResponse(jd);
    }

    // ── Enrich ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void enrich(UUID id, JdEnrichRequest request) {
        log.debug("Enriching JD: {}", id);
        JobDescription jd = findJdOrThrow(id);

        jd.setFieldOfStudy(request.getFieldOfStudy());
        jd.setCertificationsRequired(request.getCertificationsRequired());
        jd.setCertificationsGoodToHave(request.getCertificationsGoodToHave());
        jd.setCtcRange(request.getCtcRange());
        jd.setPayFrequency(request.getPayFrequency());
        jd.setEquityEsop(request.getEquityEsop());
        jd.setBenefits(request.getBenefits());
        jd.setDepartment(request.getDepartment());
        jd.setReportsTo(request.getReportsTo());
        jd.setIndustryDomain(request.getIndustryDomain());
        jd.setPreferredPriorRoles(request.getPreferredPriorRoles());
        jd.setPreferredCompanyTypes(request.getPreferredCompanyTypes());
        jd.setRoleSummary(request.getRoleSummary());
        jd.setKeyResponsibilities(request.getKeyResponsibilities());
        jd.setDomainExpertise(request.getDomainExpertise());
        jd.setSkillsBehavioural(request.getSkillsBehavioural());
        jd.setUpdatedOn(Instant.now());
        jd.setUpdatedBy(SecurityUtils.getCurrentEmail().orElse("system"));

        jobDescriptionRepository.save(jd);

        if (request.getEnrichedJd() != null) {
            JobDescriptionSummary summary = jobDescriptionSummaryRepository
                    .findByJobDescriptionId(id)
                    .orElseGet(() -> { JobDescriptionSummary s = new JobDescriptionSummary(); s.setJobDescriptionId(id); return s; });
            summary.setEnrichedJd(request.getEnrichedJd());
            jobDescriptionSummaryRepository.save(summary);
        }

        log.info("JD {} enriched successfully", id);
    }

    // ── Configure ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void configure(UUID id, JdConfigRequest request) {
        log.debug("Configuring JD: {}", id);
        JobDescription jd = findJdOrThrow(id);

        jd.setJdConfig(request.getJdConfig());
        if (request.getSkillsDomainSpecific() != null) {
            jd.setSkillsDomainSpecific(request.getSkillsDomainSpecific());
        }
        jd.setUpdatedOn(Instant.now());
        jd.setUpdatedBy(SecurityUtils.getCurrentEmail().orElse("system"));

        jobDescriptionRepository.save(jd);

        if (request.getMustHaveSkills() != null) {
            JobDescriptionSummary summary = jobDescriptionSummaryRepository
                    .findByJobDescriptionId(id)
                    .orElseGet(() -> { JobDescriptionSummary s = new JobDescriptionSummary(); s.setJobDescriptionId(id); return s; });
            summary.setNonNegotiableRules(objectMapper.valueToTree(
                    Map.of("mustHaveSkills", request.getMustHaveSkills(),
                           "useConfigMustHave", Boolean.TRUE.equals(request.getUseConfigMustHave()))));
            jobDescriptionSummaryRepository.save(summary);
        }

        log.info("JD {} configured successfully", id);
    }

    // ── Scorecard ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void updateScorecard(UUID id, JdScorecardRequest request) {
        log.debug("Updating scorecard for JD: {}", id);
        findJdOrThrow(id);

        JobDescriptionSummary summary = jobDescriptionSummaryRepository
                .findByJobDescriptionId(id)
                .orElseGet(() -> { JobDescriptionSummary s = new JobDescriptionSummary(); s.setJobDescriptionId(id); return s; });

        summary.setScorecardRoleClarity(request.getScorecardRoleClarity());
        summary.setScorecardTechSpecificity(request.getScorecardTechSpecificity());
        summary.setConsultingVsProduct(request.getConsultingVsProduct());
        summary.setExperienceInflation(request.getExperienceInflation());
        summary.setMissingScreening(request.getMissingScreening());
        summary.setCompensationSignal(request.getCompensationSignal());
        summary.setMlVerdict(request.getMlVerdict());
        summary.setJudgeResults(request.getJudgeResults());
        summary.setMlReflectionLoop(request.getMlReflectionLoop());

        jobDescriptionSummaryRepository.save(summary);
        log.info("Scorecard updated for JD: {}", id);
    }

    // ── Existing methods ───────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<JobDescriptionResponse> findAll() {
        log.debug("Fetching all job descriptions");
        List<JobDescription> jds = jobDescriptionRepository.findAll();
        if (jds.isEmpty()) return List.of();

        Set<UUID> companyIds = jds.stream().map(JobDescription::getCompanyId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        Set<UUID> jobTitleIds = jds.stream().map(JobDescription::getJobTitleId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        Set<UUID> skillIds = jds.stream()
                .flatMap(jd -> Stream.concat(
                        jd.getRequiredSkills() != null ? jd.getRequiredSkills().stream() : Stream.empty(),
                        jd.getGoodToHaveSkills() != null ? jd.getGoodToHaveSkills().stream() : Stream.empty()))
                .collect(Collectors.toSet());

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
                .map(cj -> buildCandidateResponse(
                        candidateMap.get(cj.getCandidateId()),
                        evaluationMap.get(cj.getId()),
                        interviewMap.getOrDefault(cj.getId(), List.of()),
                        scoreMap.get(cj.getCandidateId()),
                        designationNames, companyNames))
                .sorted(Comparator.comparing(
                        r -> r.getEvaluation() != null && r.getEvaluation().getJdOverallMatchScore() != null
                                ? r.getEvaluation().getJdOverallMatchScore() : BigDecimal.ZERO,
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelJobDescription(UUID id) {
        log.debug("Cancelling job description: {}", id);
        JobDescription jd = findJdOrThrow(id);
        jd.setStatus("Cancelled");
        jobDescriptionRepository.save(jd);
        log.info("Job description {} marked as Cancelled", id);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void validateRequiredFields(MlJdData jdData) {
        List<String> missing = new ArrayList<>();

        // job_title_id NOT NULL — requires role_title
        if (jdData.getRoleTitle() == null || jdData.getRoleTitle().isBlank())
            missing.add("role_title");

        // job_description_text NOT NULL — requires summary_responsibilities
        if (jdData.getSummaryResponsibilities() == null || jdData.getSummaryResponsibilities().isEmpty())
            missing.add("summary_responsibilities");

        // job_type maps to job_type column
        if (jdData.getJobType() == null || jdData.getJobType().isBlank())
            missing.add("job_type");

        // job_level maps to job_level column
        if (jdData.getJobLevel() == null || jdData.getJobLevel().isBlank())
            missing.add("job_level");

        // mandatory skills — at least one skill in any category
        MlMandatorySkills ms = jdData.getMandatorySkills();
        boolean hasAnySkill = ms != null && (
                !safeList(ms.getProgrammingLanguages()).isEmpty() ||
                !safeList(ms.getFrameworksAndLibraries()).isEmpty() ||
                !safeList(ms.getTools()).isEmpty() ||
                !safeList(ms.getDatabases()).isEmpty() ||
                !safeList(ms.getCloudAndInfra()).isEmpty()
        );
        if (!hasAnySkill)
            missing.add("mandatory_skills");

        if (!missing.isEmpty()) {
            log.warn("JD validation failed - missing fields: {}", missing);
            throw new JdMissingFieldsException(missing);
        }
    }

    private JobDescription findJdOrThrow(UUID id) {
        return jobDescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job description not found: " + id));
    }

    private UUID resolveJobTitle(String title, String jobLevel) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Job title is required from ML response");
        }
        String trimmed = title.trim();
        return jobTitleRepository.findByNameIgnoreCase(trimmed)
                .orElseGet(() -> {
                    log.info("Auto-creating job title: {}", trimmed);
                    String dept = inferDepartment(trimmed);
                    String seniority = normalizeJobLevel(jobLevel);
                    return jobTitleRepository.save(new JobTitle(trimmed, dept, seniority));
                })
                .getId();
    }

    private List<UUID> resolveSkillIds(List<String> names, String category, boolean isTechnical) {
        if (names == null || names.isEmpty()) return List.of();

        List<String> trimmed = names.stream()
                .filter(n -> n != null && !n.isBlank())
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        Map<String, UUID> existing = skillRepository.findByNameIgnoreCaseIn(trimmed).stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), Skill::getId));

        List<UUID> result = new ArrayList<>();
        for (String name : trimmed) {
            UUID id = existing.get(name.toLowerCase());
            if (id == null) {
                log.info("Auto-creating skill: {} [{}]", name, category);
                String dept = inferDepartment(name);
                id = skillRepository.save(new Skill(name, category, dept, isTechnical)).getId();
            }
            result.add(id);
        }
        return result;
    }

    private String inferDepartment(String text) {
        if (text == null) return "Engineering";
        String lower = text.toLowerCase();
        if (lower.matches(".*(data|analytics|etl|pipeline|spark|databricks|hadoop|hive|glue|airflow|kafka|bigdata|big data).*"))
            return "Data Engineering";
        if (lower.matches(".*(frontend|react|angular|vue|css|html|ui|ux|flutter|swift|ios|android|mobile).*"))
            return "Frontend Engineering";
        if (lower.matches(".*(backend|java|spring|node|python|go|rust|ruby|api|microservice|sql|postgresql|mongodb).*"))
            return "Backend Engineering";
        return "Engineering";
    }

    private String normalizeJobLevel(String jobLevel) {
        if (jobLevel == null) return null;
        return switch (jobLevel.trim().toLowerCase()) {
            case "junior", "entry" -> "Junior";
            case "mid", "middle"   -> "Mid";
            case "senior"          -> "Senior";
            case "lead"            -> "Lead";
            case "manager"         -> "Manager";
            default                -> null;
        };
    }

    private String normalizeWorkMode(String workMode) {
        if (workMode == null) return null;
        return switch (workMode.trim().toLowerCase()) {
            case "in office", "office", "onsite", "on-site", "on site" -> "Onsite";
            case "wfh", "work from home", "remote"                     -> "Remote";
            case "hybrid"                                               -> "Hybrid";
            default -> null;
        };
    }

    private String generateCode() {
        long count = jobDescriptionRepository.count();
        return String.format("JD-%05d", count + 1);
    }

    private String buildDescriptionText(List<String> responsibilities, String roleTitle) {
        if (responsibilities != null && !responsibilities.isEmpty()) {
            return String.join("\n", responsibilities);
        }
        return roleTitle != null ? roleTitle : "Parsed from document";
    }

    private <T> List<T> safeList(List<T> list) {
        return list != null ? list : List.of();
    }

    private JobDescriptionResponse buildCreateResponse(JobDescription jd) {
        String companyName = companyRepository.findById(jd.getCompanyId())
                .map(c -> c.getName()).orElse(null);
        String jobTitleName = jobTitleRepository.findById(jd.getJobTitleId())
                .map(t -> t.getName()).orElse(null);

        List<UUID> allSkillIds = Stream.of(
                safeList(jd.getRequiredSkills()),
                safeList(jd.getGoodToHaveSkills())
        ).flatMap(Collection::stream).distinct().collect(Collectors.toList());

        Map<UUID, String> skillNames = skillRepository.findAllById(allSkillIds).stream()
                .collect(Collectors.toMap(s -> s.getId(), s -> s.getName()));

        List<String> requiredSkillNames = safeList(jd.getRequiredSkills()).stream()
                .map(id -> skillNames.getOrDefault(id, id.toString())).collect(Collectors.toList());
        List<String> goodToHaveSkillNames = safeList(jd.getGoodToHaveSkills()).stream()
                .map(id -> skillNames.getOrDefault(id, id.toString())).collect(Collectors.toList());

        return JobDescriptionResponse.builder()
                .id(jd.getId())
                .code(jd.getCode())
                .company(companyName)
                .jobTitle(jobTitleName)
                .jobType(jd.getJobType())
                .jobMode(jd.getJobMode())
                .jobLevel(jd.getJobLevel())
                .jobDescriptionText(jd.getJobDescriptionText())
                .summaryResponsibilities(jd.getSummaryResponsibilities())
                .experienceMinYrs(jd.getExperienceMinYrs())
                .experienceMaxYrs(jd.getExperienceMaxYrs())
                .requiredSkills(requiredSkillNames)
                .goodToHaveSkills(goodToHaveSkillNames)
                .totalPositions(jd.getTotalPositions())
                .totalPositionsSelected(jd.getTotalPositionsSelected())
                .totalRounds(jd.getTotalRounds())
                .status(jd.getStatus())
                .createdOn(jd.getCreatedOn())
                .updatedOn(jd.getUpdatedOn())
                .createdBy(jd.getCreatedBy())
                .updatedBy(jd.getUpdatedBy())
                .build();
    }

    private JdCandidateResponse buildCandidateResponse(Candidate candidate, CandidateJobEvaluation evaluation,
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
                        .id(i.getId()).roundNumber(i.getRoundNumber())
                        .interviewerName(i.getInterviewerName()).interviewerEmail(i.getInterviewerEmail())
                        .interviewerType(i.getInterviewerType()).scheduledOn(i.getScheduledOn())
                        .mode(i.getMode()).status(i.getStatus()).questions(i.getQuestions())
                        .focusAreas(i.getFocusAreas()).feedback(i.getFeedback())
                        .score(i.getScore()).maxScore(i.getMaxScore()).build())
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
