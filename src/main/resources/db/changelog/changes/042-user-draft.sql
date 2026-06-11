--liquibase formatted sql

--changeset tvarah:042-1 labels:v0.0.1 context:dml
--comment: Add Draft status to user_status lookup table
INSERT INTO user_status (name, description) VALUES
    ('Draft', 'User record exists in the system but has no platform access and no Keycloak account');
--rollback DELETE FROM user_status WHERE name = 'Draft';
