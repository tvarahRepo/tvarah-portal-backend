--liquibase formatted sql

--changeset tvarah:011-1 labels:v0.0.1 context:ddl
--comment: Create user_status lookup table
CREATE TABLE user_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_user_status PRIMARY KEY (name)
);
--rollback DROP TABLE user_status;

--changeset tvarah:011-2 labels:v0.0.1 context:dml
--comment: Seed user_status data
INSERT INTO user_status (name, description) VALUES
    ('Active',    'User is active and can log in'),
    ('Inactive',  'User account is deactivated'),
    ('Suspended', 'User account is temporarily suspended'),
    ('Pending',   'User registration is pending approval');
--rollback DELETE FROM user_status;
