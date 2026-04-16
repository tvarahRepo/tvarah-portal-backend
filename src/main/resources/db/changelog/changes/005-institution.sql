--liquibase formatted sql

--changeset tvarah:005-1 labels:v0.0.1 context:ddl
--comment: Create institution lookup table with self-referencing parent
CREATE TABLE institution (
    id                    UUID         NOT NULL DEFAULT gen_random_uuid(),
    name                  VARCHAR(200) NOT NULL,
    type                  VARCHAR(100),
    parent_institution_id UUID,
    alias                 VARCHAR(200),
    category              VARCHAR(100),
    tier                  VARCHAR(100),
    city                  VARCHAR(150) NOT NULL,
    state                 VARCHAR(150) NOT NULL,
    country               VARCHAR(150),
    ranking               VARCHAR(100),
    is_verified           BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_institution        PRIMARY KEY (id),
    CONSTRAINT fk_institution_parent FOREIGN KEY (parent_institution_id) REFERENCES institution (id)
);
CREATE INDEX idx_institution_name                  ON institution (name);
CREATE INDEX idx_institution_type                  ON institution (type);
CREATE INDEX idx_institution_parent_institution_id ON institution (parent_institution_id);
CREATE INDEX idx_institution_alias                 ON institution (alias);
CREATE INDEX idx_institution_category              ON institution (category);
CREATE INDEX idx_institution_tier                  ON institution (tier);
CREATE INDEX idx_institution_city                  ON institution (city);
CREATE INDEX idx_institution_state                 ON institution (state);
CREATE INDEX idx_institution_country               ON institution (country);
CREATE INDEX idx_institution_ranking               ON institution (ranking);
--rollback DROP TABLE institution;
