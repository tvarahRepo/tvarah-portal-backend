--liquibase formatted sql

--changeset tvarah:007-1 labels:v0.0.1 context:ddl
--comment: Create job_title reference table
CREATE TABLE job_title (
    id              UUID         NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(150) NOT NULL,
    department      VARCHAR(100) NOT NULL,
    seniority_level VARCHAR(50),
    CONSTRAINT pk_job_title            PRIMARY KEY (id),
    CONSTRAINT fk_job_title_department FOREIGN KEY (department) REFERENCES department (name)
);
CREATE INDEX idx_job_title_name            ON job_title (name);
CREATE INDEX idx_job_title_department      ON job_title (department);
CREATE INDEX idx_job_title_seniority_level ON job_title (seniority_level);
--rollback DROP TABLE job_title;
