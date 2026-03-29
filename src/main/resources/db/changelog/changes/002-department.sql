--liquibase formatted sql

--changeset tvarah:002-1 labels:v0.0.1 context:ddl
--comment: Create department lookup table with self-referencing parent
CREATE TABLE department (
    id                UUID         NOT NULL DEFAULT gen_random_uuid(),
    name              VARCHAR(100) NOT NULL,
    code              VARCHAR(20)  NOT NULL,
    parent_department VARCHAR(100),
    CONSTRAINT pk_department         PRIMARY KEY (id),
    CONSTRAINT uq_department_name    UNIQUE (name),
    CONSTRAINT uq_department_code    UNIQUE (code),
    CONSTRAINT fk_department_parent  FOREIGN KEY (parent_department) REFERENCES department (name)
);
CREATE INDEX idx_department_name             ON department (name);
CREATE INDEX idx_department_parent_department ON department (parent_department);
--rollback DROP TABLE department;
