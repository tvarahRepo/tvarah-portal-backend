--liquibase formatted sql

--changeset tvarah:018-1 labels:v0.0.1 context:ddl
--comment: Create user table (quoted to avoid reserved word conflict in PostgreSQL)
CREATE TABLE "user" (
    id               UUID         NOT NULL DEFAULT gen_random_uuid(),
    first_name       VARCHAR(150),
    last_name        VARCHAR(150),
    email            VARCHAR(255) NOT NULL,
    role             VARCHAR(50)  NOT NULL,
    status           VARCHAR(100),
    created_on       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_on       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT pk_user        PRIMARY KEY (id),
    CONSTRAINT uq_user_email  UNIQUE (email),
    CONSTRAINT fk_user_status FOREIGN KEY (status) REFERENCES user_status (name)
);
CREATE INDEX idx_user_email  ON "user" (email);
CREATE INDEX idx_user_role   ON "user" (role);
CREATE INDEX idx_user_status ON "user" (status);
--rollback DROP TABLE "user";
