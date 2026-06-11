--liquibase formatted sql
--changeset tvarah:040-1 labels:v0.0.1 context:ddl
--comment: Add logo storage columns to company table
ALTER TABLE company
    ADD COLUMN logo               BYTEA,
    ADD COLUMN logo_content_type  VARCHAR(100);
--rollback ALTER TABLE company DROP COLUMN logo, DROP COLUMN logo_content_type;
