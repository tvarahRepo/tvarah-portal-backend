--liquibase formatted sql

--changeset tvarah:026-1 labels:v0.0.1 context:ddl
--comment: Create candidate_experience table
CREATE TABLE candidate_experience (
    id               UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id     UUID         NOT NULL,
    company_id       UUID,
    designation_id   UUID,
    industry_id      UUID,
    job_title_id     UUID,
    start_date       DATE         NOT NULL,
    end_date         DATE,
    is_current       BOOLEAN      NOT NULL DEFAULT FALSE,
    role_description TEXT,
    work_location    VARCHAR(150),
    employment_type  VARCHAR(50),
    CONSTRAINT pk_candidate_experience PRIMARY KEY (id),
    CONSTRAINT fk_ce_candidate_id      FOREIGN KEY (candidate_id)   REFERENCES candidate (id),
    CONSTRAINT fk_ce_company_id        FOREIGN KEY (company_id)     REFERENCES company (id),
    CONSTRAINT fk_ce_designation_id    FOREIGN KEY (designation_id) REFERENCES designation (id),
    CONSTRAINT fk_ce_industry_id       FOREIGN KEY (industry_id)    REFERENCES industry (id),
    CONSTRAINT fk_ce_job_title_id      FOREIGN KEY (job_title_id)   REFERENCES job_title (id)
);
CREATE INDEX idx_ce_candidate_id    ON candidate_experience (candidate_id);
CREATE INDEX idx_ce_company_id      ON candidate_experience (company_id);
CREATE INDEX idx_ce_designation_id  ON candidate_experience (designation_id);
CREATE INDEX idx_ce_industry_id     ON candidate_experience (industry_id);
CREATE INDEX idx_ce_job_title_id    ON candidate_experience (job_title_id);
--rollback DROP TABLE candidate_experience;
