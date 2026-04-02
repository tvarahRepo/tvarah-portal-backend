--liquibase formatted sql

--changeset tvarah:021-1 labels:v0.0.1 context:ddl
--comment: Create candidate table
CREATE TABLE candidate (
    id                  UUID           NOT NULL DEFAULT gen_random_uuid(),
    code                SERIAL         NOT NULL,
    first_name          VARCHAR(150)   NOT NULL,
    last_name           VARCHAR(150)   NOT NULL,
    email               VARCHAR(255),
    address             TEXT,
    city                VARCHAR(150),
    country             VARCHAR(150),
    phone_number        VARCHAR(20)    NOT NULL,
    whatsapp_number     VARCHAR(20),
    status               VARCHAR(100)   NOT NULL,
    job_title_id         UUID           NOT NULL,
    designation_id       UUID           NOT NULL,
    current_company_id   UUID           NOT NULL,
    total_experience     DECIMAL(12,2),
    relevant_experience  DECIMAL(12,2),
    resume_url           TEXT,
    created_on           TIMESTAMPTZ    NOT NULL,
    updated_on           TIMESTAMPTZ,
    created_by           VARCHAR(200)   NOT NULL,
    updated_by           VARCHAR(200),
    CONSTRAINT pk_candidate                  PRIMARY KEY (id),
    CONSTRAINT uq_candidate_email            UNIQUE (email),
    CONSTRAINT fk_candidate_status           FOREIGN KEY (status)             REFERENCES candidate_status (name),
    CONSTRAINT fk_candidate_job_title_id     FOREIGN KEY (job_title_id)       REFERENCES job_title (id),
    CONSTRAINT fk_candidate_designation_id   FOREIGN KEY (designation_id)     REFERENCES designation (id),
    CONSTRAINT fk_candidate_current_company  FOREIGN KEY (current_company_id) REFERENCES company (id)
);
CREATE INDEX idx_candidate_email               ON candidate (email);
CREATE INDEX idx_candidate_phone_number        ON candidate (phone_number);
CREATE INDEX idx_candidate_whatsapp_number     ON candidate (whatsapp_number);
CREATE INDEX idx_candidate_status              ON candidate (status);
CREATE INDEX idx_candidate_job_title_id        ON candidate (job_title_id);
CREATE INDEX idx_candidate_designation_id      ON candidate (designation_id);
CREATE INDEX idx_candidate_current_company_id  ON candidate (current_company_id);
CREATE INDEX idx_candidate_total_experience    ON candidate (total_experience);
CREATE INDEX idx_candidate_relevant_experience ON candidate (relevant_experience);
CREATE INDEX idx_candidate_created_on          ON candidate (created_on);
--rollback DROP TABLE candidate;
