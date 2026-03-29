--liquibase formatted sql

--changeset tvarah:009-1 labels:v0.0.1 context:ddl
--comment: Create skill reference table
CREATE TABLE skill (
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    name         VARCHAR(200) NOT NULL,
    category     VARCHAR(200) NOT NULL,
    tier         VARCHAR(50),
    department   VARCHAR(100) NOT NULL,
    is_technical BOOLEAN      NOT NULL DEFAULT FALSE,
    is_verified  BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_skill            PRIMARY KEY (id),
    CONSTRAINT fk_skill_department FOREIGN KEY (department) REFERENCES department (name)
);
CREATE INDEX idx_skill_name       ON skill (name);
CREATE INDEX idx_skill_category   ON skill (category);
CREATE INDEX idx_skill_tier       ON skill (tier);
CREATE INDEX idx_skill_department ON skill (department);
--rollback DROP TABLE skill;
