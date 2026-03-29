--liquibase formatted sql

--changeset tvarah:008-1 labels:v0.0.1 context:ddl
--comment: Create designation reference table
CREATE TABLE designation (
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    name       VARCHAR(150) NOT NULL,
    level      VARCHAR(50),
    category   VARCHAR(100),
    department VARCHAR(100),
    alias      VARCHAR(150),
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_designation            PRIMARY KEY (id),
    CONSTRAINT uq_designation_name       UNIQUE (name),
    CONSTRAINT fk_designation_department FOREIGN KEY (department) REFERENCES department (name)
);
CREATE INDEX idx_designation_name       ON designation (name);
CREATE INDEX idx_designation_level      ON designation (level);
CREATE INDEX idx_designation_category   ON designation (category);
CREATE INDEX idx_designation_department ON designation (department);
--rollback DROP TABLE designation;
