--liquibase formatted sql

--changeset tvarah:025-1 labels:v0.0.1 context:ddl
--comment: Create candidate_resume_summary table (1:1 with candidate)
CREATE TABLE candidate_resume_summary (
    id                       UUID        NOT NULL DEFAULT gen_random_uuid(),
    candidate_id             UUID        NOT NULL,
    skill_depth              VARCHAR(30) NOT NULL,
    skill_depth_tags         TEXT[]      NOT NULL,
    experience_quality       VARCHAR(30) NOT NULL,
    experience_quality_tags  TEXT[]      NOT NULL,
    academic_background      VARCHAR(30) NOT NULL,
    academic_background_tags TEXT[]      NOT NULL,
    domain_depth             VARCHAR(30) NOT NULL,
    domain_depth_tags        TEXT[]      NOT NULL,
    parsed_resume            JSONB,
    weakness                 TEXT[],
    strength                 TEXT[],
    CONSTRAINT pk_candidate_resume_summary PRIMARY KEY (id),
    CONSTRAINT fk_crs_candidate_id         FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_crs_candidate_id ON candidate_resume_summary (candidate_id);
--rollback DROP TABLE candidate_resume_summary;

--changeset tvarah:025-2 labels:v0.0.1 context:ddl
--comment: Create candidate_intelligence_insight table (1:N with candidate)
CREATE TABLE candidate_intelligence_insight (
    id                                            UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id                                  UUID         NOT NULL,
    decision_signal                               VARCHAR(20)  CHECK (decision_signal IN ('Strong', 'Proceed', 'Hold', 'Reject')),
    priority_level                                VARCHAR(20)  CHECK (priority_level IN ('Low', 'Moderate', 'High')),
    decision_rationale                            TEXT,
    candidate_fit_summary_executive_summary       TEXT,
    candidate_fit_summary_risk_indicators         TEXT[],
    education_assessment_credibility_level        VARCHAR(20)  CHECK (education_assessment_credibility_level IN ('Weak', 'Moderate', 'Strong')),
    institution_verification_status               VARCHAR(20)  CHECK (institution_verification_status IN ('Verified', 'Unverified', 'Suspicious')),
    timeline_integrity                            VARCHAR(30)  CHECK (timeline_integrity IN ('Clean', 'Minor Gaps', 'Significant Gaps')),
    education_assessment_executive_summary        TEXT,
    education_assessment_hiring_impact            TEXT,
    education_assessment_risk_indicators          JSONB,
    experience_credibility_level                  VARCHAR(20)  CHECK (experience_credibility_level IN ('Low', 'Medium', 'High')),
    employment_stability                          VARCHAR(20)  CHECK (employment_stability IN ('Stable', 'Moderate', 'Unstable')),
    experience_assessment_executive_summary       TEXT,
    experience_assessment_hiring_impact           TEXT,
    experience_assessment_risk_indicators         JSONB,
    technical_capability_level                    VARCHAR(20)  CHECK (technical_capability_level IN ('Advanced', 'Intermediate', 'Basic', 'Insufficient')),
    skill_authenticity_level                      VARCHAR(20)  CHECK (skill_authenticity_level IN ('High', 'Medium', 'Low')),
    skills_assessment_executive_summary           TEXT,
    skills_assessment_hiring_impact               TEXT,
    skills_assessment_risk_indicators             JSONB,
    domain_assessment_primary_domain              VARCHAR(100),
    domain_impact_level                           VARCHAR(20)  CHECK (domain_impact_level IN ('High', 'Medium', 'Low')),
    domain_assessment_executive_summary           TEXT,
    domain_assessment_hiring_impact               TEXT,
    domain_assessment_risk_indicators             JSONB,
    education_insights                            JSONB,
    experience_insights                           JSONB,
    CONSTRAINT pk_candidate_intelligence_insight PRIMARY KEY (id),
    CONSTRAINT fk_cii_candidate_id               FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_cii_candidate_id     ON candidate_intelligence_insight (candidate_id);
CREATE INDEX idx_cii_decision_signal  ON candidate_intelligence_insight (decision_signal);
CREATE INDEX idx_cii_priority_level   ON candidate_intelligence_insight (priority_level);
--rollback DROP TABLE candidate_intelligence_insight;
