--liquibase formatted sql

--changeset tvarah:041-1 labels:v0.0.1 context:ddl
--comment: Create user_role reference table
CREATE TABLE user_role (
    name        VARCHAR(50)  NOT NULL,
    type        VARCHAR(20)  NOT NULL,
    description TEXT,
    CONSTRAINT pk_user_role      PRIMARY KEY (name),
    CONSTRAINT chk_user_role_type CHECK (type IN ('INTERNAL', 'EXTERNAL'))
);
--rollback DROP TABLE user_role;

--changeset tvarah:041-2 labels:v0.0.1 context:dml
--comment: Seed all platform roles
INSERT INTO user_role (name, type, description) VALUES
    ('Admin',                  'INTERNAL', 'Full platform access — manages users, roles, and system configuration'),
    ('Management',             'INTERNAL', 'Senior internal stakeholders with read access across all accounts'),
    ('Account Manager',        'INTERNAL', 'Owns client relationships and oversees hiring delivery for assigned companies'),
    ('Recruiter',              'INTERNAL', 'Sources, screens, and manages candidates across job descriptions'),
    ('Panel',                  'INTERNAL', 'Subject-matter experts who conduct technical interviews and submit evaluations'),
    ('Sales and BD',           'INTERNAL', 'Business development team responsible for acquiring new client accounts'),
    ('Candidate',              'INTERNAL', 'Job seekers registered on the platform'),
    ('Client-TA Head',         'EXTERNAL', 'Client-side Talent Acquisition Head — approves JDs and hiring decisions'),
    ('Client-TA Associate',    'EXTERNAL', 'Client-side TA Associate — coordinates interviews and candidate feedback'),
    ('Client-Hiring Manager',  'EXTERNAL', 'Client-side Hiring Manager — defines job requirements and makes final calls'),
    ('Client-Director',        'EXTERNAL', 'Client-side Director — senior stakeholder with visibility into hiring pipeline');
--rollback DELETE FROM user_role;
