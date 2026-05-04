--liquibase formatted sql

--changeset tvarah:020-1 labels:v0.0.1 context:ddl
--comment: Create job_description table
CREATE TABLE job_description (
    id                           UUID           NOT NULL DEFAULT gen_random_uuid(),
    code                         VARCHAR(10)    NOT NULL,
    company_id                   UUID           NOT NULL,
    job_title_id                 UUID           NOT NULL,
    job_type                     VARCHAR(50)    CHECK (job_type IN ('Full Time', 'Part Time', 'Contract', 'Internship')),
    job_mode                     VARCHAR(50)    CHECK (job_mode IN ('Onsite', 'Hybrid', 'Remote')),
    job_level                    VARCHAR(50)    CHECK (job_level IN ('Junior', 'Mid', 'Senior', 'Lead', 'Manager')),
    job_description_text         TEXT           NOT NULL,
    summary_responsibilities     TEXT[],
    experience_min_yrs           DECIMAL(4,1),
    experience_max_yrs           DECIMAL(4,1),
    required_skills              UUID[],
    good_to_have_skills          UUID[],
    salary_min                   DECIMAL(10,2),
    salary_max                   DECIMAL(10,2),
    total_positions              INT            NOT NULL DEFAULT 2,
    total_positions_selected     INT            NOT NULL DEFAULT 0,
    total_rounds                 INT            NOT NULL DEFAULT 2,
    status                       VARCHAR(100)   NOT NULL,
    closed_on                    TIMESTAMPTZ,
    created_on                   TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_on                   TIMESTAMPTZ    NOT NULL DEFAULT now(),
    created_by                   VARCHAR(200),
    updated_by                   VARCHAR(200),

    -- Section A: Location
    location_city                VARCHAR(100),
    location_country             VARCHAR(100),
    locations                    JSONB,

    -- Section B: Education & Certs
    degree_required              VARCHAR(100),
    field_of_study               VARCHAR(200),
    certifications_required      TEXT[],
    certifications_good_to_have  TEXT[],
    notice_period                VARCHAR(50),

    -- Section C: Skills sub-categories
    skills_programming_languages UUID[],
    skills_frameworks_libraries  UUID[],
    skills_tools                 UUID[],
    skills_databases             UUID[],
    skills_cloud_infra           UUID[],
    skills_domain_specific       UUID[],
    skills_behavioural           TEXT[],

    -- Section D: Compensation
    ctc_range                    VARCHAR(100),
    pay_frequency                VARCHAR(20),
    equity_esop                  VARCHAR(200),
    benefits                     TEXT[],

    -- Section E: Role & Org Info
    department                   VARCHAR(100),
    reports_to                   VARCHAR(100),
    industry_domain              VARCHAR(200),
    preferred_prior_roles        TEXT[],
    preferred_company_types      TEXT[],
    role_summary                 TEXT,
    key_responsibilities         TEXT[],
    hiring_deadline              VARCHAR(50),
    domain_expertise             VARCHAR(200),

    -- Section F: JD Config
    jd_config                    JSONB,

    CONSTRAINT pk_job_description PRIMARY KEY (id),
    CONSTRAINT uq_jd_code         UNIQUE (code),
    CONSTRAINT fk_jd_company_id   FOREIGN KEY (company_id)   REFERENCES company (id),
    CONSTRAINT fk_jd_job_title_id FOREIGN KEY (job_title_id) REFERENCES job_title (id),
    CONSTRAINT fk_jd_status       FOREIGN KEY (status)       REFERENCES job_status (name)
);
CREATE INDEX idx_jd_code         ON job_description (code);
CREATE INDEX idx_jd_company_id   ON job_description (company_id);
CREATE INDEX idx_jd_job_title_id ON job_description (job_title_id);
CREATE INDEX idx_jd_status       ON job_description (status);
--rollback DROP TABLE job_description;

--changeset tvarah:020-2 labels:v0.0.1 context:ddl
--comment: Create job_description_summary table (1:1 with job_description)
CREATE TABLE job_description_summary (
    id                       UUID           NOT NULL DEFAULT gen_random_uuid(),
    job_description_id       UUID           NOT NULL,
    parsed_jd                JSONB,
    enriched_jd              JSONB,
    jd_score                 DECIMAL(12,2),
    non_negotiable_rules     JSONB,

    -- Section G: ML Scorecard
    scorecard_role_clarity   SMALLINT,
    scorecard_tech_specificity SMALLINT,
    consulting_vs_product    VARCHAR(20),
    experience_inflation     VARCHAR(10),
    missing_screening        TEXT[],
    compensation_signal      VARCHAR(100),

    -- Section H: ML Verdict
    ml_verdict               VARCHAR(10),
    judge_results            JSONB,
    ml_reflection_loop       SMALLINT,

    CONSTRAINT pk_jd_summary PRIMARY KEY (id),
    CONSTRAINT uq_jds_jd_id  UNIQUE (job_description_id),
    CONSTRAINT fk_jds_jd_id  FOREIGN KEY (job_description_id) REFERENCES job_description (id)
);
CREATE INDEX idx_jds_jd_id ON job_description_summary (job_description_id);
--rollback DROP TABLE job_description_summary;
