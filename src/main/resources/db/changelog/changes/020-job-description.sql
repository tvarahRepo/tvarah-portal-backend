--liquibase formatted sql

--changeset tvarah:020-1 labels:v0.0.1 context:ddl
--comment: Create job_description table
CREATE TABLE job_description (
    id                       UUID           NOT NULL DEFAULT gen_random_uuid(),
    code                     VARCHAR(10)    NOT NULL,
    company_id               UUID,
    job_title_id             UUID           NOT NULL,
    job_type                 VARCHAR(50)    CHECK (job_type IN ('Full Time', 'Part Time', 'Contract', 'Internship')),
    job_mode                 VARCHAR(50)    CHECK (job_mode IN ('Onsite', 'Hybrid', 'Remote')),
    job_level                VARCHAR(50)    CHECK (job_level IN ('Junior', 'Mid', 'Senior', 'Lead', 'Manager')),
    job_description_text     TEXT           NOT NULL,
    summary_responsibilities TEXT[],
    experience_min_yrs       DECIMAL(4,1),
    experience_max_yrs       DECIMAL(4,1),
    required_skills          UUID[],
    good_to_have_skills      UUID[],
    salary_min               DECIMAL(10,2),
    salary_max               DECIMAL(10,2),
    total_positions          INT            NOT NULL DEFAULT 2,
    total_positions_selected INT            NOT NULL DEFAULT 0,
    total_rounds             INT            NOT NULL DEFAULT 2,
    status                   VARCHAR(100)   NOT NULL,
    closed_on                TIMESTAMPTZ,
    created_on               TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_on               TIMESTAMPTZ    NOT NULL DEFAULT now(),
    created_by               VARCHAR(200),
    updated_by               VARCHAR(200),
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
    id                   UUID           NOT NULL DEFAULT gen_random_uuid(),
    job_description_id   UUID           NOT NULL,
    parsed_jd            JSONB,
    enriched_jd          JSONB,
    jd_score             DECIMAL(12,2),
    non_negotiable_rules JSONB,
    CONSTRAINT pk_jd_summary PRIMARY KEY (id),
    CONSTRAINT uq_jds_jd_id  UNIQUE (job_description_id),
    CONSTRAINT fk_jds_jd_id  FOREIGN KEY (job_description_id) REFERENCES job_description (id)
);
CREATE INDEX idx_jds_jd_id ON job_description_summary (job_description_id);
--rollback DROP TABLE job_description_summary;
