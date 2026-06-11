--liquibase formatted sql

--changeset tvarah:043-1 labels:v0.0.1 context:ddl
--comment: Add assigned_to_user_id column to job_description
ALTER TABLE job_description
    ADD COLUMN IF NOT EXISTS assigned_to_user_id UUID;
--rollback ALTER TABLE job_description DROP COLUMN IF EXISTS assigned_to_user_id;
