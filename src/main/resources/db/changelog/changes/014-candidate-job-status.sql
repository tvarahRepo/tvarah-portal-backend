--liquibase formatted sql

--changeset tvarah:014-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job_status lookup table
CREATE TABLE candidate_job_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_candidate_job_status PRIMARY KEY (name)
);
--rollback DROP TABLE candidate_job_status;

--changeset tvarah:014-2 labels:v0.0.1 context:dml
--comment: Seed candidate_job_status data
INSERT INTO candidate_job_status (name, description) VALUES
    ('Sourced',         'Candidate sourced for the job, not yet reviewed'),
    ('Shortlisted',     'Candidate selected based on resume parsing and score'),
    ('Backlog',         'Candidate parked for this job to process later'),
    ('Active Internal', 'Candidate in internal process from telephonic onwards till panel'),
    ('Potential',       'Candidate cleared panel and moved to client interview stage'),
    ('Panel Done',      'Panel interview completed'),
    ('Active Round',    'Candidate active in a current interview round'),
    ('Convert',         'Offer received by candidate'),
    ('Joined',          'Candidate has joined'),
    ('On Hold',         'Candidate process is on hold'),
    ('Withdrawn',       'Candidate has withdrawn from the process'),
    ('Flagged',         'Candidate flagged for fraud, work-ex forging, panel/client cheating or layoff');
--rollback DELETE FROM candidate_job_status;
