--liquibase formatted sql

--changeset tvarah:029-1 labels:v0.0.1 context:ddl
--comment: Create candidate_skill table
CREATE TABLE candidate_skill (
    id                UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_id      UUID         NOT NULL,
    skill             VARCHAR(150) NOT NULL,
    proficiency_level VARCHAR(50),
    years_experience  DECIMAL(4,1) CHECK (years_experience >= 0),
    last_used_year    SMALLINT,
    validation_status VARCHAR(100) NOT NULL,
    CONSTRAINT pk_candidate_skill       PRIMARY KEY (id),
    CONSTRAINT uq_candidate_skill       UNIQUE (candidate_id, skill),
    CONSTRAINT fk_csk_candidate_id      FOREIGN KEY (candidate_id)      REFERENCES candidate (id),
    CONSTRAINT fk_csk_skill             FOREIGN KEY (skill)             REFERENCES skill (name),
    CONSTRAINT fk_csk_validation_status FOREIGN KEY (validation_status) REFERENCES skill_validation_status (name)
);
CREATE INDEX idx_csk_candidate_id      ON candidate_skill (candidate_id);
CREATE INDEX idx_csk_skill             ON candidate_skill (skill);
CREATE INDEX idx_csk_validation_status ON candidate_skill (validation_status);
--rollback DROP TABLE candidate_skill;
