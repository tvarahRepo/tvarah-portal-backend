--liquibase formatted sql

--changeset tvarah:013-1 labels:v0.0.1 context:ddl
--comment: Create candidate_status lookup table
CREATE TABLE candidate_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_candidate_status PRIMARY KEY (name)
);
--rollback DROP TABLE candidate_status;

--changeset tvarah:013-2 labels:v0.0.1 context:dml
--comment: Seed candidate_status data
INSERT INTO candidate_status (name, description) VALUES
    ('Sourced',           'Candidate has been sourced, not yet processed'),
    ('Backlog',           'Candidate parked for processing later'),
    ('Active',            'Candidate is actively being processed'),
    ('Converted',         'Candidate has been successfully placed'),
    ('Completed 90 Days', 'Candidate has completed 90 days on the job'),
    ('Flagged',           'Candidate flagged for fraud, work-ex forging, panel/client cheating or layoff');
--rollback DELETE FROM candidate_status;
