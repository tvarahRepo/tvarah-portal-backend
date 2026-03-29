--liquibase formatted sql

--changeset tvarah:025-1 labels:v0.0.1 context:ddl
--comment: Create candidate_score table (1:1 with candidate)
CREATE TABLE candidate_score (
    id                                    UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id                          UUID         NOT NULL,
    overall_score                         DECIMAL(5,2),
    overall_strength                      VARCHAR(50),
    education_quality_score               DECIMAL(5,2),
    institution_credibility_score         DECIMAL(5,2),
    academic_alignment_score              DECIMAL(5,2),
    timeline_integrity_score              DECIMAL(5,2),
    experience_quality_score              DECIMAL(5,2),
    company_quality_signal_score          DECIMAL(5,2),
    role_progression_score                DECIMAL(5,2),
    experience_sufficiency_score          DECIMAL(5,2),
    skill_depth_score                     DECIMAL(5,2),
    depth_of_usage_score                  DECIMAL(5,2),
    breadth_tooling_clarity_score         DECIMAL(5,2),
    domain_depth_score                    DECIMAL(5,2),
    domain_consistency_score              DECIMAL(5,2),
    domain_impact_evidence_score          DECIMAL(5,2),
    domain_language_signal_strength_score DECIMAL(5,2),
    feedback_comments                     TEXT,
    drop_flag                             BOOLEAN      NOT NULL DEFAULT FALSE,
    fraud_risk                            VARCHAR(50),
    fraud_status                          BOOLEAN      NOT NULL DEFAULT FALSE,
    updated_on                            TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT pk_candidate_score  PRIMARY KEY (id),
    CONSTRAINT fk_cs_candidate_id  FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_cs_candidate_id  ON candidate_score (candidate_id);
CREATE INDEX idx_cs_overall_score ON candidate_score (overall_score);
CREATE INDEX idx_cs_fraud_risk    ON candidate_score (fraud_risk);
CREATE INDEX idx_cs_fraud_status  ON candidate_score (fraud_status);
CREATE INDEX idx_cs_updated_on    ON candidate_score (updated_on);
--rollback DROP TABLE candidate_score;
