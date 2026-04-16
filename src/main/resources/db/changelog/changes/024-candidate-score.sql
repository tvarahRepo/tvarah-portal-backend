--liquibase formatted sql

--changeset tvarah:024-1 labels:v0.0.1 context:ddl
--comment: Create candidate_score table (1:1 with candidate)
CREATE TABLE candidate_score (
    id                UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id      UUID         NOT NULL,
    score             DECIMAL(5,2),
    max_score         DECIMAL(5,2),
    strength          VARCHAR(50),
    feedback_comments TEXT,
    drop_flag         BOOLEAN      NOT NULL DEFAULT FALSE,
    drop_flag_message TEXT,
    fraud_risk        VARCHAR(50),
    fraud_status      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_on        TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_on        TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT pk_candidate_score PRIMARY KEY (id),
    CONSTRAINT fk_cs_candidate_id FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_cs_candidate_id ON candidate_score (candidate_id);
CREATE INDEX idx_cs_score        ON candidate_score (score);
CREATE INDEX idx_cs_fraud_risk   ON candidate_score (fraud_risk);
CREATE INDEX idx_cs_fraud_status ON candidate_score (fraud_status);
CREATE INDEX idx_cs_updated_on   ON candidate_score (updated_on);
--rollback DROP TABLE candidate_score;
