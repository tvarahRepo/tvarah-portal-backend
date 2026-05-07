--changeset tvarah:037-1 labels:v0.0.1 context:ddl
ALTER TABLE "user"
    ADD COLUMN keycloak_user_id VARCHAR(36),
    ADD CONSTRAINT uq_user_keycloak_id UNIQUE (keycloak_user_id);
CREATE INDEX idx_user_keycloak_id ON "user" (keycloak_user_id);
--rollback ALTER TABLE "user" DROP COLUMN keycloak_user_id;
