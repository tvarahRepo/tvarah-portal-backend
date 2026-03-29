--liquibase formatted sql

--changeset tvarah:006-1 labels:v0.0.1 context:ddl
--comment: Create company reference table
CREATE TABLE company (
    id                  UUID          NOT NULL DEFAULT gen_random_uuid(),
    name                VARCHAR(200)  NOT NULL,
    name_alias          VARCHAR(200),
    type                VARCHAR(100),
    industry            VARCHAR(150),
    headquarter_city    VARCHAR(100),
    headquarter_state   VARCHAR(100),
    headquarter_country VARCHAR(100),
    founded_year        INT,
    no_of_employees     INT,
    review_count        INT           NOT NULL DEFAULT 0,
    avg_rating          DECIMAL(3,1)  CHECK (avg_rating >= 0 AND avg_rating <= 5),
    mca_verified        BOOLEAN       NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_company          PRIMARY KEY (id),
    CONSTRAINT uq_company_name     UNIQUE (name),
    CONSTRAINT fk_company_industry FOREIGN KEY (industry) REFERENCES industry (name)
);
CREATE INDEX idx_company_name       ON company (name);
CREATE INDEX idx_company_name_alias ON company (name_alias);
CREATE INDEX idx_company_type       ON company (type);
CREATE INDEX idx_company_industry   ON company (industry);
--rollback DROP TABLE company;
