--liquibase formatted sql

--changeset tvarah:014-1 labels:v0.0.1 context:ddl
--comment: Create candidate_status lookup table
CREATE TABLE candidate_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_candidate_status PRIMARY KEY (name)
);
--rollback DROP TABLE candidate_status;
