--liquibase formatted sql

--changeset tvarah:036-1 labels:v0.0.1 context:ddl
--comment: Widen degree_required and field_of_study to TEXT to handle long ML-extracted values
ALTER TABLE job_description
    ALTER COLUMN degree_required TYPE TEXT,
    ALTER COLUMN field_of_study  TYPE TEXT;
--rollback ALTER TABLE job_description ALTER COLUMN degree_required TYPE VARCHAR(100), ALTER COLUMN field_of_study TYPE VARCHAR(200);
