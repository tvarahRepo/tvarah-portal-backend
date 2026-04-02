--liquibase formatted sql

--changeset tvarah:025-1 labels:v0.0.1 context:ddl
--comment: Create candidate_resume_summary table (1:1 with candidate)
CREATE TABLE candidate_resume_summary (
    id                       UUID        NOT NULL DEFAULT gen_random_uuid(),
    candidate_id             UUID        NOT NULL,
    skill_depth              VARCHAR(30) NOT NULL,
    skill_depth_tags         TEXT[]      NOT NULL,
    experience_quality       VARCHAR(30) NOT NULL,
    experience_quality_tags  TEXT[]      NOT NULL,
    academic_background      VARCHAR(30) NOT NULL,
    academic_background_tags TEXT[]      NOT NULL,
    domain_depth             VARCHAR(30) NOT NULL,
    domain_depth_tags        TEXT[]      NOT NULL,
    CONSTRAINT pk_candidate_resume_summary PRIMARY KEY (id),
    CONSTRAINT fk_crs_candidate_id         FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_crs_candidate_id ON candidate_resume_summary (candidate_id);
--rollback DROP TABLE candidate_resume_summary;
