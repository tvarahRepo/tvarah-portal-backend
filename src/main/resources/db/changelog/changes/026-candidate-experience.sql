--liquibase formatted sql

--changeset tvarah:026-1 labels:v0.0.1 context:ddl
--comment: Create candidate_experience table
CREATE TABLE candidate_experience (
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id       UUID         NOT NULL,
    company            VARCHAR(200),
    designation        VARCHAR(150),
    industry           VARCHAR(150),
    job_title_id       UUID,
    start_date         DATE         NOT NULL,
    end_date           DATE,
    is_current         BOOLEAN      NOT NULL DEFAULT FALSE,
    experience_summary TEXT,
    CONSTRAINT pk_candidate_experience PRIMARY KEY (id),
    CONSTRAINT fk_ce_candidate_id      FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    CONSTRAINT fk_ce_company           FOREIGN KEY (company)      REFERENCES company (name),
    CONSTRAINT fk_ce_designation       FOREIGN KEY (designation)  REFERENCES designation (name),
    CONSTRAINT fk_ce_industry          FOREIGN KEY (industry)     REFERENCES industry (name),
    CONSTRAINT fk_ce_job_title_id      FOREIGN KEY (job_title_id) REFERENCES job_title (id)
);
CREATE INDEX idx_ce_candidate_id ON candidate_experience (candidate_id);
CREATE INDEX idx_ce_company      ON candidate_experience (company);
CREATE INDEX idx_ce_designation  ON candidate_experience (designation);
CREATE INDEX idx_ce_industry     ON candidate_experience (industry);
CREATE INDEX idx_ce_job_title_id ON candidate_experience (job_title_id);
--rollback DROP TABLE candidate_experience;
