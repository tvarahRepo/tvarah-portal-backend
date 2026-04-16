--liquibase formatted sql

--changeset tvarah:004-1 labels:v0.0.1 context:ddl
--comment: Create specialization lookup table with optional eligible degree FK
CREATE TABLE specialization (
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    name               VARCHAR(150) NOT NULL,
    alias              VARCHAR(100),
    eligible_degree_id UUID,
    CONSTRAINT pk_specialization       PRIMARY KEY (id),
    CONSTRAINT uq_specialization_name  UNIQUE (name),
    CONSTRAINT fk_spec_eligible_degree FOREIGN KEY (eligible_degree_id) REFERENCES degree (id)
);
CREATE INDEX idx_specialization_name               ON specialization (name);
CREATE INDEX idx_specialization_eligible_degree_id ON specialization (eligible_degree_id);
--rollback DROP TABLE specialization;
