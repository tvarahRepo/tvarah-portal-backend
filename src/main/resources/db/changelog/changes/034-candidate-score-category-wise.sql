--liquibase formatted sql

--changeset tvarah:034-1 labels:v0.0.1 context:ddl
--comment: Create candidate_score_category_wise table (1:N with candidate_score)
CREATE TABLE candidate_score_category_wise (
    id                UUID         NOT NULL DEFAULT gen_random_uuid(),
    candidate_score_id UUID        NOT NULL,
    label             VARCHAR(150) NOT NULL,
    score             DECIMAL(5,2),
    max_score         DECIMAL(5,2),
    CONSTRAINT pk_candidate_score_category_wise    PRIMARY KEY (id),
    CONSTRAINT fk_cscw_candidate_score_id          FOREIGN KEY (candidate_score_id) REFERENCES candidate_score (id)
);
CREATE INDEX idx_cscw_candidate_score_id ON candidate_score_category_wise (candidate_score_id);
CREATE INDEX idx_cscw_label              ON candidate_score_category_wise (label);
--rollback DROP TABLE candidate_score_category_wise;
