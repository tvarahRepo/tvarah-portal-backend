--liquibase formatted sql

--changeset tvarah:038-1 labels:v0.0.1 context:ddl
--comment: Add phone_number, department, location columns to user table
ALTER TABLE "user"
    ADD COLUMN phone_number VARCHAR(20),
    ADD COLUMN department   VARCHAR(100),
    ADD COLUMN location     VARCHAR(255),
    ADD CONSTRAINT fk_user_department FOREIGN KEY (department) REFERENCES department (name);
CREATE INDEX idx_user_department ON "user" (department);
--rollback ALTER TABLE "user" DROP COLUMN phone_number, DROP COLUMN department, DROP COLUMN location;
