--liquibase formatted sql

--changeset tvarah:012-1 labels:v0.0.1 context:ddl
--comment: Create client_status lookup table
CREATE TABLE client_status (
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT pk_client_status PRIMARY KEY (name)
);
--rollback DROP TABLE client_status;
