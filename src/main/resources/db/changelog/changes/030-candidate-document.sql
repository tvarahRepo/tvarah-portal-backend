--liquibase formatted sql

--changeset tvarah:030-1 labels:v0.0.1 context:ddl
--comment: Create candidate_document table
CREATE TABLE candidate_document (
    id           UUID        NOT NULL DEFAULT gen_random_uuid(),
    candidate_id UUID        NOT NULL,
    type         VARCHAR(50) NOT NULL,
    url          TEXT        NOT NULL,
    uploaded_on  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT pk_candidate_document PRIMARY KEY (id),
    CONSTRAINT fk_cdoc_candidate_id  FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_cdoc_candidate_id ON candidate_document (candidate_id);
CREATE INDEX idx_cdoc_type         ON candidate_document (type);
--rollback DROP TABLE candidate_document;
