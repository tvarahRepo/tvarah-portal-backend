--liquibase formatted sql

--changeset tvarah:020-1 labels:v0.0.1 context:ddl
--comment: Create client table
CREATE TABLE client (
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    name         VARCHAR(200) NOT NULL,
    industry     VARCHAR(100),
    size         INT,
    recruiter_id UUID,
    status       VARCHAR(100) NOT NULL,
    created_on   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_on   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT pk_client              PRIMARY KEY (id),
    CONSTRAINT uq_client_name         UNIQUE (name),
    CONSTRAINT fk_client_industry     FOREIGN KEY (industry)     REFERENCES industry (name),
    CONSTRAINT fk_client_recruiter_id FOREIGN KEY (recruiter_id) REFERENCES "user" (id),
    CONSTRAINT fk_client_status       FOREIGN KEY (status)       REFERENCES client_status (name)
);
CREATE INDEX idx_client_name         ON client (name);
CREATE INDEX idx_client_industry     ON client (industry);
CREATE INDEX idx_client_recruiter_id ON client (recruiter_id);
CREATE INDEX idx_client_status       ON client (status);
--rollback DROP TABLE client;
