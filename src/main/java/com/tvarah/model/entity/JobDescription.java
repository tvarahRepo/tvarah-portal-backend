package com.tvarah.model.entity;

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
}
