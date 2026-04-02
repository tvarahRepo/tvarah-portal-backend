--liquibase formatted sql

--changeset tvarah:012-1 labels:v0.0.1 context:ddl
--comment: Create job_status lookup table
CREATE TABLE job_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_job_status PRIMARY KEY (name)
);
--rollback DROP TABLE job_status;

--changeset tvarah:012-2 labels:v0.0.1 context:dml
--comment: Seed job_status data
INSERT INTO job_status (name, description) VALUES
    ('Draft',     'Job is saved as draft, not yet published'),
    ('Open',      'Job is open and accepting candidates'),
    ('On Hold',   'Job is temporarily paused'),
    ('Closed',    'Job has been filled or closed'),
    ('Cancelled', 'Job has been cancelled');
--rollback DELETE FROM job_status;
