--liquibase formatted sql

--changeset tvarah:013-1 labels:v0.0.1 context:ddl
--comment: Create job_status lookup table
CREATE TABLE job_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_job_status PRIMARY KEY (name)
);
--rollback DROP TABLE job_status;
