--liquibase formatted sql

--changeset tvarah:033-1 labels:v0.0.1 context:ddl
--comment: Create candidate_job_evaluation table (1:1 with candidate_job)
CREATE TABLE candidate_job_evaluation (
    id                     UUID          NOT NULL DEFAULT gen_random_uuid(),
    candidate_job_id       UUID          NOT NULL,
    jd_overall_match_score DECIMAL(5,2),
    skill_match_score      DECIMAL(5,2),
    domain_match_score     DECIMAL(5,2),
    jd_exp_relevance_score DECIMAL(5,2),
    status                 VARCHAR(100)  NOT NULL,
    CONSTRAINT pk_candidate_job_evaluation PRIMARY KEY (id),
    CONSTRAINT fk_cje_candidate_job_id     FOREIGN KEY (candidate_job_id) REFERENCES candidate_job (id),
    CONSTRAINT fk_cje_status               FOREIGN KEY (status)           REFERENCES candidate_job_evaluation_status (name)
);
CREATE INDEX idx_cje_candidate_job_id       ON candidate_job_evaluation (candidate_job_id);
CREATE INDEX idx_cje_jd_overall_match_score ON candidate_job_evaluation (jd_overall_match_score);
CREATE INDEX idx_cje_skill_match_score      ON candidate_job_evaluation (skill_match_score);
CREATE INDEX idx_cje_domain_match_score     ON candidate_job_evaluation (domain_match_score);
CREATE INDEX idx_cje_jd_exp_relevance_score ON candidate_job_evaluation (jd_exp_relevance_score);
CREATE INDEX idx_cje_status                 ON candidate_job_evaluation (status);
--rollback DROP TABLE candidate_job_evaluation;
