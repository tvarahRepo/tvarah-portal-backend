--liquibase formatted sql

--changeset tvarah:037-1 labels:v0.0.1 context:ddl
--comment: Add location, education, skills sub-category, compensation, role info, and config columns to job_description

ALTER TABLE job_description
    ADD COLUMN location_city                VARCHAR(100),
    ADD COLUMN location_country             VARCHAR(100),
    ADD COLUMN locations                    JSONB,
    ADD COLUMN degree_required              VARCHAR(100),
    ADD COLUMN field_of_study               VARCHAR(200),
    ADD COLUMN certifications_required      TEXT[],
    ADD COLUMN certifications_good_to_have  TEXT[],
    ADD COLUMN notice_period                VARCHAR(50),
    ADD COLUMN skills_programming_languages UUID[],
    ADD COLUMN skills_frameworks_libraries  UUID[],
    ADD COLUMN skills_tools                 UUID[],
    ADD COLUMN skills_databases             UUID[],
    ADD COLUMN skills_cloud_infra           UUID[],
    ADD COLUMN skills_domain_specific       UUID[],
    ADD COLUMN skills_behavioural           TEXT[],
    ADD COLUMN ctc_range                    VARCHAR(100),
    ADD COLUMN pay_frequency                VARCHAR(20),
    ADD COLUMN equity_esop                  VARCHAR(200),
    ADD COLUMN benefits                     TEXT[],
    ADD COLUMN department                   VARCHAR(100),
    ADD COLUMN reports_to                   VARCHAR(100),
    ADD COLUMN industry_domain              VARCHAR(200),
    ADD COLUMN preferred_prior_roles        TEXT[],
    ADD COLUMN preferred_company_types      TEXT[],
    ADD COLUMN role_summary                 TEXT,
    ADD COLUMN key_responsibilities         TEXT[],
    ADD COLUMN hiring_deadline              VARCHAR(50),
    ADD COLUMN domain_expertise             VARCHAR(200),
    ADD COLUMN jd_config                    JSONB;

--rollback ALTER TABLE job_description DROP COLUMN location_city, DROP COLUMN location_country, DROP COLUMN locations, DROP COLUMN degree_required, DROP COLUMN field_of_study, DROP COLUMN certifications_required, DROP COLUMN certifications_good_to_have, DROP COLUMN notice_period, DROP COLUMN skills_programming_languages, DROP COLUMN skills_frameworks_libraries, DROP COLUMN skills_tools, DROP COLUMN skills_databases, DROP COLUMN skills_cloud_infra, DROP COLUMN skills_domain_specific, DROP COLUMN skills_behavioural, DROP COLUMN ctc_range, DROP COLUMN pay_frequency, DROP COLUMN equity_esop, DROP COLUMN benefits, DROP COLUMN department, DROP COLUMN reports_to, DROP COLUMN industry_domain, DROP COLUMN preferred_prior_roles, DROP COLUMN preferred_company_types, DROP COLUMN role_summary, DROP COLUMN key_responsibilities, DROP COLUMN hiring_deadline, DROP COLUMN domain_expertise, DROP COLUMN jd_config;

--changeset tvarah:037-2 labels:v0.0.1 context:ddl
--comment: Add scorecard and ML verdict columns to job_description_summary

ALTER TABLE job_description_summary
    ADD COLUMN scorecard_role_clarity     SMALLINT,
    ADD COLUMN scorecard_tech_specificity SMALLINT,
    ADD COLUMN consulting_vs_product      VARCHAR(20),
    ADD COLUMN experience_inflation       VARCHAR(10),
    ADD COLUMN missing_screening          TEXT[],
    ADD COLUMN compensation_signal        VARCHAR(100),
    ADD COLUMN ml_verdict                 VARCHAR(10),
    ADD COLUMN judge_results              JSONB,
    ADD COLUMN ml_reflection_loop         SMALLINT;

--rollback ALTER TABLE job_description_summary DROP COLUMN scorecard_role_clarity, DROP COLUMN scorecard_tech_specificity, DROP COLUMN consulting_vs_product, DROP COLUMN experience_inflation, DROP COLUMN missing_screening, DROP COLUMN compensation_signal, DROP COLUMN ml_verdict, DROP COLUMN judge_results, DROP COLUMN ml_reflection_loop;
