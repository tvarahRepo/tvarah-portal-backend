--liquibase formatted sql

--changeset tvarah:019-1 labels:v0.0.1 context:ddl
--comment: Create company_user table — maps internal platform users to companies with roles
CREATE TABLE company_user (
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    company_id UUID        NOT NULL,
    user_id    UUID        NOT NULL,
    role       VARCHAR(50) NOT NULL,
    CONSTRAINT pk_company_user  PRIMARY KEY (id),
    CONSTRAINT fk_cu_company_id FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT fk_cu_user_id    FOREIGN KEY (user_id)    REFERENCES "user" (id)
);
CREATE INDEX idx_cu_company_id ON company_user (company_id);
CREATE INDEX idx_cu_user_id    ON company_user (user_id);
CREATE INDEX idx_cu_role       ON company_user (role);
--rollback DROP TABLE company_user;
