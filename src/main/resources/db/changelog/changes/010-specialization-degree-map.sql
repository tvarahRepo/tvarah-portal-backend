--liquibase formatted sql

--changeset tvarah:010-1 labels:v0.0.1 context:ddl
--comment: Create specialization_degree_map junction table
CREATE TABLE specialization_degree_map (
    id                UUID NOT NULL DEFAULT gen_random_uuid(),
    specialization_id UUID NOT NULL,
    degree_id         UUID NOT NULL,
    CONSTRAINT pk_specialization_degree_map PRIMARY KEY (id),
    CONSTRAINT uq_specialization_degree_map UNIQUE (specialization_id, degree_id),
    CONSTRAINT fk_sdm_specialization        FOREIGN KEY (specialization_id) REFERENCES specialization (id),
    CONSTRAINT fk_sdm_degree                FOREIGN KEY (degree_id)         REFERENCES degree (id)
);
CREATE INDEX idx_sdm_specialization_id ON specialization_degree_map (specialization_id);
CREATE INDEX idx_sdm_degree_id         ON specialization_degree_map (degree_id);
--rollback DROP TABLE specialization_degree_map;
