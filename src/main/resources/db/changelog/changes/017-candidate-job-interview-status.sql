--liquibase formatted sql

--changeset tvarah:017-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job_interview_status lookup table
CREATE TABLE candidate_job_interview_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_candidate_job_interview_status PRIMARY KEY (name)
);
--rollback DROP TABLE candidate_job_interview_status;
