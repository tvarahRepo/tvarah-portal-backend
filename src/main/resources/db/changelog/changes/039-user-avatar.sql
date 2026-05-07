--liquibase formatted sql

--changeset tvarah:039-1 labels:v0.0.1 context:ddl
--comment: Add avatar storage columns to user table
ALTER TABLE "user"
    ADD COLUMN avatar               BYTEA,
    ADD COLUMN avatar_content_type  VARCHAR(100);
--rollback ALTER TABLE "user" DROP COLUMN avatar, DROP COLUMN avatar_content_type;
