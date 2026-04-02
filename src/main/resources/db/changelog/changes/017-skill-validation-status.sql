--liquibase formatted sql

--changeset tvarah:017-1 labels:v0.0.1 context:ddl
--comment: Create skill_validation_status lookup table
CREATE TABLE skill_validation_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_skill_validation_status PRIMARY KEY (name)
);
--rollback DROP TABLE skill_validation_status;

--changeset tvarah:017-2 labels:v0.0.1 context:dml
--comment: Seed skill_validation_status data
INSERT INTO skill_validation_status (name, description) VALUES
    ('Unverified', 'Skill has not been verified'),
    ('Pending',    'Skill verification is in progress'),
    ('Verified',   'Skill has been verified'),
    ('Rejected',   'Skill verification was rejected');
--rollback DELETE FROM skill_validation_status;
