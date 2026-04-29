package com.tvarah.model.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.tvarah.model.ml.MlLocation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class JdDraftDto {

    // ── Metadata ───────────────────────────────────────────────────────────────
    private UUID companyId;
    private Integer totalPositions;
    private Integer totalRounds;

    // ── Core ───────────────────────────────────────────────────────────────────
    private String roleTitle;
    private String jobType;
    private String jobMode;
    private String jobLevel;
    private BigDecimal experienceMinYrs;
    private BigDecimal experienceMaxYrs;
    private List<String> summaryResponsibilities;

    // ── Section A: Location ────────────────────────────────────────────────────
    private String locationCity;
    private String locationCountry;
    private List<MlLocation> locations;

    // ── Section B: Education & Certs ───────────────────────────────────────────
    private String degreeRequired;
    private String fieldOfStudy;
    private List<String> certificationsRequired;
    private List<String> certificationsGoodToHave;

    // ── Section C: Skills (as names — resolved to UUIDs on save) ──────────────
    private List<String> skillsProgrammingLanguages;
    private List<String> skillsFrameworksLibraries;
    private List<String> skillsTools;
    private List<String> skillsDatabases;
    private List<String> skillsCloudInfra;
    private List<String> goodToHaveSkills;
    private List<String> skillsBehavioural;

    // ── Section D: Compensation ────────────────────────────────────────────────
    private String ctcRange;
    private String payFrequency;
    private String equityEsop;
    private List<String> benefits;

    // ── Section E: Role & Org Info ─────────────────────────────────────────────
    private String department;
    private String reportsTo;
    private String industryDomain;
    private List<String> preferredPriorRoles;
    private List<String> preferredCompanyTypes;
    private String roleSummary;
    private List<String> keyResponsibilities;
    private String domainExpertise;

    // ── ML Verdict (stored in job_description_summary) ─────────────────────────
    private String mlVerdict;
    private Integer mlReflectionLoop;
    private JsonNode judgeResults;
    private JsonNode parsedJd;
}
