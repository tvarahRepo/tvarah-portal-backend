package com.tvarah.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "job_description")
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "job_title_id", nullable = false)
    private UUID jobTitleId;

    @Column(name = "job_type", length = 50)
    private String jobType;

    @Column(name = "job_mode", length = 50)
    private String jobMode;

    @Column(name = "job_level", length = 50)
    private String jobLevel;

    @Column(name = "job_description_text", nullable = false, columnDefinition = "TEXT")
    private String jobDescriptionText;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "summary_responsibilities", columnDefinition = "text[]")
    private List<String> summaryResponsibilities;

    @Column(name = "experience_min_yrs", precision = 4, scale = 1)
    private BigDecimal experienceMinYrs;

    @Column(name = "experience_max_yrs", precision = 4, scale = 1)
    private BigDecimal experienceMaxYrs;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "required_skills", columnDefinition = "uuid[]")
    private List<UUID> requiredSkills;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "good_to_have_skills", columnDefinition = "uuid[]")
    private List<UUID> goodToHaveSkills;

    @Column(name = "salary_min", precision = 10, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 10, scale = 2)
    private BigDecimal salaryMax;

    @Column(name = "total_positions", nullable = false)
    private Integer totalPositions;

    @Column(name = "total_positions_selected", nullable = false)
    private Integer totalPositionsSelected;

    @Column(name = "total_rounds", nullable = false)
    private Integer totalRounds;

    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @Column(name = "closed_on")
    private Instant closedOn;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

    @Column(name = "created_by", length = 200)
    private String createdBy;

    @Column(name = "updated_by", length = 200)
    private String updatedBy;

    // ── Section A: Location ────────────────────────────────────────────────────

    @Column(name = "location_city", length = 100)
    private String locationCity;

    @Column(name = "location_country", length = 100)
    private String locationCountry;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "locations", columnDefinition = "jsonb")
    private JsonNode locations;

    // ── Section B: Education & Certs ───────────────────────────────────────────

    @Column(name = "degree_required", length = 100)
    private String degreeRequired;

    @Column(name = "field_of_study", length = 200)
    private String fieldOfStudy;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "certifications_required", columnDefinition = "text[]")
    private List<String> certificationsRequired;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "certifications_good_to_have", columnDefinition = "text[]")
    private List<String> certificationsGoodToHave;

    @Column(name = "notice_period", length = 50)
    private String noticePeriod;

    // ── Section C: Skills sub-categories ──────────────────────────────────────

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_programming_languages", columnDefinition = "uuid[]")
    private List<UUID> skillsProgrammingLanguages;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_frameworks_libraries", columnDefinition = "uuid[]")
    private List<UUID> skillsFrameworksLibraries;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_tools", columnDefinition = "uuid[]")
    private List<UUID> skillsTools;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_databases", columnDefinition = "uuid[]")
    private List<UUID> skillsDatabases;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_cloud_infra", columnDefinition = "uuid[]")
    private List<UUID> skillsCloudInfra;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_domain_specific", columnDefinition = "uuid[]")
    private List<UUID> skillsDomainSpecific;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "skills_behavioural", columnDefinition = "text[]")
    private List<String> skillsBehavioural;

    // ── Section D: Compensation ────────────────────────────────────────────────

    @Column(name = "ctc_range", length = 100)
    private String ctcRange;

    @Column(name = "pay_frequency", length = 20)
    private String payFrequency;

    @Column(name = "equity_esop", length = 200)
    private String equityEsop;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "benefits", columnDefinition = "text[]")
    private List<String> benefits;

    // ── Section E: Role & Org Info ─────────────────────────────────────────────

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "reports_to", length = 100)
    private String reportsTo;

    @Column(name = "industry_domain", length = 200)
    private String industryDomain;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "preferred_prior_roles", columnDefinition = "text[]")
    private List<String> preferredPriorRoles;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "preferred_company_types", columnDefinition = "text[]")
    private List<String> preferredCompanyTypes;

    @Column(name = "role_summary", columnDefinition = "TEXT")
    private String roleSummary;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "key_responsibilities", columnDefinition = "text[]")
    private List<String> keyResponsibilities;

    @Column(name = "hiring_deadline", length = 50)
    private String hiringDeadline;

    @Column(name = "domain_expertise", length = 200)
    private String domainExpertise;

    // ── Section F: JD Config ───────────────────────────────────────────────────

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "jd_config", columnDefinition = "jsonb")
    private JsonNode jdConfig;
}
