--liquibase formatted sql

--changeset tvarah:022-1 labels:v0.0.1 context:ddl
--comment: Create candidate_preference table (1:1 with candidate)
CREATE TABLE candidate_preference (
    id                         UUID           NOT NULL DEFAULT gen_random_uuid(),
    candidate_id               UUID           NOT NULL,
    current_location           VARCHAR(100),
    primary_preferred_location VARCHAR(100),
    preferred_locations        TEXT[],
    can_relocate               BOOLEAN        NOT NULL DEFAULT FALSE,
    preferred_job_title_id     UUID,
    current_ctc                DECIMAL(12,2),
    expected_ctc_min           DECIMAL(12,2),
    expected_ctc_max           DECIMAL(12,2),
    ctc_currency               VARCHAR(5)     NOT NULL DEFAULT 'INR',
    work_mode_preference       VARCHAR(50),
    work_authorization         BOOLEAN        NOT NULL DEFAULT FALSE,
    created_on                 TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_on                 TIMESTAMPTZ    NOT NULL DEFAULT now(),
    created_by                 VARCHAR(200),
    updated_by                 VARCHAR(200),
    CONSTRAINT pk_candidate_preference              PRIMARY KEY (id),
    CONSTRAINT uq_candidate_preference_candidate_id UNIQUE (candidate_id),
    CONSTRAINT fk_cp_candidate_id                   FOREIGN KEY (candidate_id)           REFERENCES candidate (id),
    CONSTRAINT fk_cp_preferred_job_title_id         FOREIGN KEY (preferred_job_title_id) REFERENCES job_title (id)
);
CREATE INDEX idx_cp_candidate_id        ON candidate_preference (candidate_id);
CREATE INDEX idx_cp_current_location    ON candidate_preference (current_location);
CREATE INDEX idx_cp_preferred_job_title ON candidate_preference (preferred_job_title_id);
CREATE INDEX idx_cp_work_mode           ON candidate_preference (work_mode_preference);
--rollback DROP TABLE candidate_preference;
