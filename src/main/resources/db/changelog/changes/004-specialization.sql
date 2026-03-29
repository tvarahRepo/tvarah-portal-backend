--liquibase formatted sql

--changeset tvarah:004-1 labels:v0.0.1 context:ddl
--comment: Create specialization lookup table
CREATE TABLE specialization (
    id    UUID         NOT NULL DEFAULT gen_random_uuid(),
    name  VARCHAR(150) NOT NULL,
    alias VARCHAR(100),
    CONSTRAINT pk_specialization      PRIMARY KEY (id),
    CONSTRAINT uq_specialization_name UNIQUE (name)
);
CREATE INDEX idx_specialization_name ON specialization (name);
--rollback DROP TABLE specialization;
