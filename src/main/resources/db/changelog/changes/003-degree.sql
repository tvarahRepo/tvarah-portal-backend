--liquibase formatted sql

--changeset tvarah:003-1 labels:v0.0.1 context:ddl
--comment: Create degree lookup table
CREATE TABLE degree (
    id       UUID         NOT NULL DEFAULT gen_random_uuid(),
    name     VARCHAR(150) NOT NULL,
    level    VARCHAR(50),
    category VARCHAR(150) NOT NULL,
    code     VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_degree      PRIMARY KEY (id),
    CONSTRAINT uq_degree_name UNIQUE (name),
    CONSTRAINT uq_degree_code UNIQUE (code)
);
CREATE INDEX idx_degree_name     ON degree (name);
CREATE INDEX idx_degree_level    ON degree (level);
CREATE INDEX idx_degree_category ON degree (category);
--rollback DROP TABLE degree;
