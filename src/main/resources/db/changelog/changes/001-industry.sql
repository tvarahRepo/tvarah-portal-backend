--liquibase formatted sql

--changeset tvarah:001-1 labels:v0.0.1 context:ddl
--comment: Create industry lookup table
CREATE TABLE industry (
    id               UUID         NOT NULL DEFAULT gen_random_uuid(),
    name             VARCHAR(150) NOT NULL,
    sector           VARCHAR(100) NOT NULL,
    naics_ref_number VARCHAR(100),
    aliases          VARCHAR(255),
    CONSTRAINT pk_industry           PRIMARY KEY (id),
    CONSTRAINT uq_industry_name      UNIQUE (name),
    CONSTRAINT uq_industry_naics_ref UNIQUE (naics_ref_number)
);
CREATE INDEX idx_industry_name   ON industry (name);
CREATE INDEX idx_industry_sector ON industry (sector);
--rollback DROP TABLE industry;
