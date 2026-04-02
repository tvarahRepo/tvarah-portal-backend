--liquibase formatted sql

--changeset tvarah:031-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job table
CREATE TABLE candidate_job (
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id UUID         NOT NULL,
    job_id       UUID         NOT NULL,
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    status       VARCHAR(100),
    created_on   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_on   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by   VARCHAR(20),
    updated_by   VARCHAR(20),
    CONSTRAINT pk_candidate_job   PRIMARY KEY (id),
    CONSTRAINT uq_candidate_job   UNIQUE (candidate_id, job_id),
    CONSTRAINT fk_cj_candidate_id FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    CONSTRAINT fk_cj_job_id       FOREIGN KEY (job_id)       REFERENCES job_description (id),
    CONSTRAINT fk_cj_status       FOREIGN KEY (status)       REFERENCES candidate_job_status (name)
);
CREATE INDEX idx_cj_candidate_id ON candidate_job (candidate_id);
CREATE INDEX idx_cj_job_id       ON candidate_job (job_id);
CREATE INDEX idx_cj_is_active    ON candidate_job (is_active);
CREATE INDEX idx_cj_status       ON candidate_job (status);
--rollback DROP TABLE candidate_job;
