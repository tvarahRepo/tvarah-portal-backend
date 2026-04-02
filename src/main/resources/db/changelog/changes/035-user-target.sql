--liquibase formatted sql

--changeset tvarah:035-1 labels:v0.0.1 context:ddl
--comment: Create user_target table
CREATE TABLE user_target (
    id               UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id          UUID        NOT NULL,
    positions_closed INT,
    sourcing         INT,
    panel_interview  INT,
    client_interview INT,
    updated_on       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT pk_user_target    PRIMARY KEY (id),
    CONSTRAINT fk_ut_user_id     FOREIGN KEY (user_id) REFERENCES "user" (id)
);
CREATE INDEX idx_ut_user_id ON user_target (user_id);
--rollback DROP TABLE user_target;
