--liquibase formatted sql

--changeset tvarah:015-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job_evaluation_status lookup table
CREATE TABLE candidate_job_evaluation_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_candidate_job_evaluation_status PRIMARY KEY (name)
);
--rollback DROP TABLE candidate_job_evaluation_status;

--changeset tvarah:015-2 labels:v0.0.1 context:dml
--comment: Seed candidate_job_evaluation_status data
INSERT INTO candidate_job_evaluation_status (name, description) VALUES
    ('Strong Match',   'Candidate is a strong match for the job'),
    ('Moderate Match', 'Candidate is a moderate match for the job'),
    ('Weak Match',     'Candidate is a weak match for the job'),
    ('Rejected',       'Candidate has been rejected for the job');
--rollback DELETE FROM candidate_job_evaluation_status;
