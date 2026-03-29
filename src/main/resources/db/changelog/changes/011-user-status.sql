--liquibase formatted sql

--changeset tvarah:011-1 labels:v0.0.1 context:ddl
--comment: Create user_status lookup table
CREATE TABLE user_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_user_status PRIMARY KEY (name)
);
--rollback DROP TABLE user_status;
