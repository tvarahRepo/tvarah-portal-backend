--liquibase formatted sql

--changeset tvarah:020-1 labels:v0.0.1 context:ddl
--comment: Create job_description table
CREATE TABLE job_description (
    id                       UUID           NOT NULL DEFAULT gen_random_uuid(),
    client_id                UUID,
    job_title_id             UUID           NOT NULL,
    job_description_text     TEXT           NOT NULL,
    experience_min_yrs       DECIMAL(4,1),
    experience_max_yrs       DECIMAL(4,1),
    required_skills          TEXT[],
    must_have_skills         TEXT[],
    good_have_skills         TEXT[],
    salary_min               DECIMAL(10,2),
    salary_max               DECIMAL(10,2),
    total_positions          INT            NOT NULL DEFAULT 2,
    total_positions_selected INT            NOT NULL DEFAULT 0,
    total_rounds             INT            NOT NULL DEFAULT 2,
    status                   VARCHAR(100)   NOT NULL,
    closed_on                TIMESTAMPTZ,
    jd_score                 DECIMAL(5,2),
    non_negotiable_rules     JSONB,
    created_on               TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_on               TIMESTAMPTZ    NOT NULL DEFAULT now(),
    created_by               VARCHAR(200),
    updated_by               VARCHAR(200),
    CONSTRAINT pk_job_description PRIMARY KEY (id),
    CONSTRAINT fk_jd_client_id    FOREIGN KEY (client_id)    REFERENCES client (id),
    CONSTRAINT fk_jd_job_title_id FOREIGN KEY (job_title_id) REFERENCES job_title (id),
    CONSTRAINT fk_jd_status       FOREIGN KEY (status)       REFERENCES job_status (name)
);
CREATE INDEX idx_jd_client_id    ON job_description (client_id);
CREATE INDEX idx_jd_job_title_id ON job_description (job_title_id);
CREATE INDEX idx_jd_status       ON job_description (status);
--rollback DROP TABLE job_description;
