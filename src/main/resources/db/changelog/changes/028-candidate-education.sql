--liquibase formatted sql

--changeset tvarah:028-1 labels:v0.0.1 context:ddl
--comment: Create candidate_education table
CREATE TABLE candidate_education (
    id             UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id   UUID         NOT NULL,
    institution_id UUID,
    degree         VARCHAR(150),
    specialization VARCHAR(150),
    start_year     SMALLINT,
    end_year       SMALLINT,
    is_highest     BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_candidate_education PRIMARY KEY (id),
    CONSTRAINT fk_ced_candidate_id    FOREIGN KEY (candidate_id)   REFERENCES candidate (id),
    CONSTRAINT fk_ced_institution_id  FOREIGN KEY (institution_id) REFERENCES institution (id),
    CONSTRAINT fk_ced_degree          FOREIGN KEY (degree)         REFERENCES degree (name),
    CONSTRAINT fk_ced_specialization  FOREIGN KEY (specialization) REFERENCES specialization (name)
);
CREATE INDEX idx_ced_candidate_id   ON candidate_education (candidate_id);
CREATE INDEX idx_ced_institution_id ON candidate_education (institution_id);
CREATE INDEX idx_ced_degree         ON candidate_education (degree);
CREATE INDEX idx_ced_specialization ON candidate_education (specialization);
--rollback DROP TABLE candidate_education;
