package com.tvarah.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "candidate_job_evaluation")
public class CandidateJobEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "candidate_job_id", nullable = false)
    private UUID candidateJobId;

    @Column(name = "jd_overall_match_score", precision = 5, scale = 2)
    private BigDecimal jdOverallMatchScore;

    @Column(name = "skill_match_score", precision = 5, scale = 2)
    private BigDecimal skillMatchScore;

    @Column(name = "domain_match_score", precision = 5, scale = 2)
    private BigDecimal domainMatchScore;

    @Column(name = "jd_exp_relevance_score", precision = 5, scale = 2)
    private BigDecimal jdExpRelevanceScore;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "max_score", precision = 5, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "status", nullable = false, length = 100)
    private String status;
}
