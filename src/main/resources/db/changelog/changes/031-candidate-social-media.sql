--liquibase formatted sql

--changeset tvarah:031-1 labels:v0.0.1 context:ddl
--comment: Create candidate_social_media table
CREATE TABLE candidate_social_media (
    id           UUID        NOT NULL DEFAULT gen_random_uuid(),
    candidate_id UUID        NOT NULL,
    platform     VARCHAR(50) NOT NULL,
    url          TEXT        NOT NULL,
    CONSTRAINT pk_candidate_social_media PRIMARY KEY (id),
    CONSTRAINT uq_candidate_social_media UNIQUE (candidate_id, platform),
    CONSTRAINT fk_csm_candidate_id       FOREIGN KEY (candidate_id) REFERENCES candidate (id)
);
CREATE INDEX idx_csm_candidate_id ON candidate_social_media (candidate_id);
CREATE INDEX idx_csm_platform     ON candidate_social_media (platform);
--rollback DROP TABLE candidate_social_media;
