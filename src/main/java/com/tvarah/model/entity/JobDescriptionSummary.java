package com.tvarah.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "job_description_summary")
public class JobDescriptionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "job_description_id", nullable = false, unique = true)
    private UUID jobDescriptionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parsed_jd", columnDefinition = "jsonb")
    private JsonNode parsedJd;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "enriched_jd", columnDefinition = "jsonb")
    private JsonNode enrichedJd;

    @Column(name = "jd_score", precision = 12, scale = 2)
    private BigDecimal jdScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "non_negotiable_rules", columnDefinition = "jsonb")
    private JsonNode nonNegotiableRules;

    // ── Section G: Scorecard ───────────────────────────────────────────────────

    @Column(name = "scorecard_role_clarity")
    private Short scorecardRoleClarity;

    @Column(name = "scorecard_tech_specificity")
    private Short scorecardTechSpecificity;

    @Column(name = "consulting_vs_product", length = 20)
    private String consultingVsProduct;

    @Column(name = "experience_inflation", length = 10)
    private String experienceInflation;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "missing_screening", columnDefinition = "text[]")
    private List<String> missingScreening;

    @Column(name = "compensation_signal", length = 100)
    private String compensationSignal;

    // ── Section H: ML Verdict ──────────────────────────────────────────────────

    @Column(name = "ml_verdict", length = 10)
    private String mlVerdict;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "judge_results", columnDefinition = "jsonb")
    private JsonNode judgeResults;

    @Column(name = "ml_reflection_loop")
    private Short mlReflectionLoop;
}
