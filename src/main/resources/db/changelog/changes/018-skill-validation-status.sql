--liquibase formatted sql

--changeset tvarah:018-1 labels:v0.0.1 context:ddl
--comment: Create skill_validation_status lookup table
CREATE TABLE skill_validation_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_skill_validation_status PRIMARY KEY (name)
);
--rollback DROP TABLE skill_validation_status;
