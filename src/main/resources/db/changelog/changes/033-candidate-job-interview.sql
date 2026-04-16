--liquibase formatted sql

--changeset tvarah:033-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job_interview table
CREATE TABLE candidate_job_interview (
    id                UUID          NOT NULL DEFAULT gen_random_uuid(),
    candidate_job_id  UUID          NOT NULL,
    round_number      INT           NOT NULL,
    interviewer_name  VARCHAR(200),
    interviewer_email VARCHAR(200),
    interviewer_type  VARCHAR(50),
    scheduled_on      TIMESTAMPTZ   NOT NULL,
    mode              VARCHAR(50),
    status            VARCHAR(100)  NOT NULL,
    questions         TEXT[],
    focus_areas       TEXT[],
    feedback          TEXT,
    score             DECIMAL(5,2),
    max_score         DECIMAL(5,2),
    created_on        TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_on        TIMESTAMPTZ   NOT NULL DEFAULT now(),
    created_by        UUID,
    updated_by        UUID,
    CONSTRAINT pk_candidate_job_interview PRIMARY KEY (id),
    CONSTRAINT fk_cji_candidate_job_id    FOREIGN KEY (candidate_job_id) REFERENCES candidate_job (id),
    CONSTRAINT fk_cji_status              FOREIGN KEY (status)           REFERENCES candidate_job_interview_status (name)
);
CREATE INDEX idx_cji_candidate_job_id ON candidate_job_interview (candidate_job_id);
CREATE INDEX idx_cji_scheduled_on     ON candidate_job_interview (scheduled_on);
CREATE INDEX idx_cji_status           ON candidate_job_interview (status);
--rollback DROP TABLE candidate_job_interview;
