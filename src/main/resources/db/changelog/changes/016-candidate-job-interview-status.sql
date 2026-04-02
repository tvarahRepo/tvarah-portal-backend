--liquibase formatted sql

--changeset tvarah:016-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job_interview_status lookup table
CREATE TABLE candidate_job_interview_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_candidate_job_interview_status PRIMARY KEY (name)
);
--rollback DROP TABLE candidate_job_interview_status;

--changeset tvarah:016-2 labels:v0.0.1 context:dml
--comment: Seed candidate_job_interview_status data
INSERT INTO candidate_job_interview_status (name, description) VALUES
    ('Scheduled',   'Interview has been scheduled'),
    ('Completed',   'Interview has been completed'),
    ('No Show',     'Candidate did not show up for the interview'),
    ('Cancelled',   'Interview has been cancelled'),
    ('Rescheduled', 'Interview has been rescheduled');
--rollback DELETE FROM candidate_job_interview_status;
