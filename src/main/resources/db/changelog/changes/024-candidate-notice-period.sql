--liquibase formatted sql

--changeset tvarah:024-1 labels:v0.0.1 context:ddl
--comment: Create candidate_notice_period table (1:1 with candidate)
CREATE TABLE candidate_notice_period (
    id                     UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id           UUID         NOT NULL,
    company                VARCHAR(200) NOT NULL,
    is_in_notice_period    BOOLEAN      NOT NULL DEFAULT FALSE,
    notice_period_duration SMALLINT     NOT NULL,
    resignation_date       TIMESTAMPTZ,
    earliest_joining_date  TIMESTAMPTZ,
    last_working_date      TIMESTAMPTZ,
    created_on             TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_on             TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by             VARCHAR(200),
    updated_by             VARCHAR(200),
    CONSTRAINT pk_candidate_notice_period              PRIMARY KEY (id),
    CONSTRAINT uq_candidate_notice_period_candidate_id UNIQUE (candidate_id),
    CONSTRAINT fk_cnp_candidate_id                     FOREIGN KEY (candidate_id) REFERENCES candidate (id),
    CONSTRAINT fk_cnp_company                          FOREIGN KEY (company)      REFERENCES company (name)
);
CREATE INDEX idx_cnp_candidate_id      ON candidate_notice_period (candidate_id);
CREATE INDEX idx_cnp_company           ON candidate_notice_period (company);
CREATE INDEX idx_cnp_resignation_date  ON candidate_notice_period (resignation_date);
CREATE INDEX idx_cnp_earliest_joining  ON candidate_notice_period (earliest_joining_date);
CREATE INDEX idx_cnp_last_working_date ON candidate_notice_period (last_working_date);
--rollback DROP TABLE candidate_notice_period;
