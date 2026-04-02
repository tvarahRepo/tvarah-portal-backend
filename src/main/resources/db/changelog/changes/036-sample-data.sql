--liquibase formatted sql

-- ─────────────────────────────────────────────────────────────
-- industry
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-1 labels:v0.0.1 context:dml
--comment: Sample data - industry
INSERT INTO industry (id, name, sector, naics_ref_number, aliases) VALUES
    ('a1000000-0000-0000-0000-000000000001', 'Information Technology', 'Technology',      '541511', 'IT, Tech'),
    ('a1000000-0000-0000-0000-000000000002', 'Banking & Finance',      'Financial',       '522110', 'BFSI, Banking'),
    ('a1000000-0000-0000-0000-000000000003', 'Healthcare',             'Life Sciences',   '621111', 'Pharma, Health'),
    ('a1000000-0000-0000-0000-000000000004', 'E-Commerce',             'Retail',          '454110', 'Ecom, Online Retail'),
    ('a1000000-0000-0000-0000-000000000005', 'Manufacturing',          'Industrial',      '311111', 'Mfg');
--rollback DELETE FROM industry WHERE id LIKE 'a1000000%';

-- ─────────────────────────────────────────────────────────────
-- department
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-2 labels:v0.0.1 context:dml
--comment: Sample data - department
INSERT INTO department (id, name, code, parent_department) VALUES
    ('a2000000-0000-0000-0000-000000000001', 'Engineering',          'ENG',  NULL),
    ('a2000000-0000-0000-0000-000000000002', 'Product',              'PRD',  NULL),
    ('a2000000-0000-0000-0000-000000000003', 'Human Resources',      'HR',   NULL),
    ('a2000000-0000-0000-0000-000000000004', 'Finance',              'FIN',  NULL),
    ('a2000000-0000-0000-0000-000000000005', 'Backend Engineering',  'BENG', 'Engineering'),
    ('a2000000-0000-0000-0000-000000000006', 'Frontend Engineering', 'FENG', 'Engineering'),
    ('a2000000-0000-0000-0000-000000000007', 'Data Engineering',     'DENG', 'Engineering');
--rollback DELETE FROM department WHERE id LIKE 'a2000000%';

-- ─────────────────────────────────────────────────────────────
-- degree
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-3 labels:v0.0.1 context:dml
--comment: Sample data - degree
INSERT INTO degree (id, name, level, category, code) VALUES
    ('a3000000-0000-0000-0000-000000000001', 'Bachelor of Technology',  'Undergraduate', 'Engineering',  'BTECH'),
    ('a3000000-0000-0000-0000-000000000002', 'Bachelor of Engineering', 'Undergraduate', 'Engineering',  'BE'),
    ('a3000000-0000-0000-0000-000000000003', 'Master of Technology',    'Postgraduate',  'Engineering',  'MTECH'),
    ('a3000000-0000-0000-0000-000000000004', 'Master of Business Administration', 'Postgraduate', 'Management', 'MBA'),
    ('a3000000-0000-0000-0000-000000000005', 'Bachelor of Science',     'Undergraduate', 'Science',      'BSC'),
    ('a3000000-0000-0000-0000-000000000006', 'Master of Computer Applications', 'Postgraduate', 'Computer Science', 'MCA');
--rollback DELETE FROM degree WHERE id LIKE 'a3000000%';

-- ─────────────────────────────────────────────────────────────
-- specialization
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-4 labels:v0.0.1 context:dml
--comment: Sample data - specialization
INSERT INTO specialization (id, name, alias) VALUES
    ('a4000000-0000-0000-0000-000000000001', 'Computer Science',          'CS'),
    ('a4000000-0000-0000-0000-000000000002', 'Information Technology',    'IT'),
    ('a4000000-0000-0000-0000-000000000003', 'Electronics & Communication', 'ECE'),
    ('a4000000-0000-0000-0000-000000000004', 'Finance',                   NULL),
    ('a4000000-0000-0000-0000-000000000005', 'Data Science',              'DS');
--rollback DELETE FROM specialization WHERE id LIKE 'a4000000%';

-- ─────────────────────────────────────────────────────────────
-- institution
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-5 labels:v0.0.1 context:dml
--comment: Sample data - institution
INSERT INTO institution (id, name, type, alias, category, tier, city, state, ranking, is_verified) VALUES
    ('a5000000-0000-0000-0000-000000000001', 'Indian Institute of Technology Bombay', 'University', 'IIT Bombay', 'Premier', 'Tier 1', 'Mumbai',    'Maharashtra', 'Top 5',  TRUE),
    ('a5000000-0000-0000-0000-000000000002', 'Indian Institute of Technology Delhi',  'University', 'IIT Delhi',  'Premier', 'Tier 1', 'New Delhi', 'Delhi',       'Top 5',  TRUE),
    ('a5000000-0000-0000-0000-000000000003', 'BITS Pilani',                           'University', 'BITS',       'Premier', 'Tier 1', 'Pilani',    'Rajasthan',   'Top 10', TRUE),
    ('a5000000-0000-0000-0000-000000000004', 'Pune University',                       'University', 'SPPU',       'State',   'Tier 2', 'Pune',      'Maharashtra', NULL,     TRUE),
    ('a5000000-0000-0000-0000-000000000005', 'VIT Vellore',                           'University', 'VIT',        'Private', 'Tier 2', 'Vellore',   'Tamil Nadu',  NULL,     TRUE);
--rollback DELETE FROM institution WHERE id LIKE 'a5000000%';

-- ─────────────────────────────────────────────────────────────
-- company
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-6 labels:v0.0.1 context:dml
--comment: Sample data - company
INSERT INTO company (id, name, name_alias, type, industry, headquarter_city, headquarter_state, headquarter_country, founded_year, no_of_employees, avg_rating, mca_verified) VALUES
    ('a6000000-0000-0000-0000-000000000001', 'Tata Consultancy Services', 'TCS',      'Public',   'Information Technology', 'Mumbai',    'Maharashtra', 'India', 1968, 600000, 4.0, TRUE),
    ('a6000000-0000-0000-0000-000000000002', 'Infosys',                   'Infosys',  'Public',   'Information Technology', 'Bengaluru', 'Karnataka',   'India', 1981, 340000, 3.9, TRUE),
    ('a6000000-0000-0000-0000-000000000003', 'Razorpay',                  'Razorpay', 'Private',  'Banking & Finance',      'Bengaluru', 'Karnataka',   'India', 2014, 3000,   4.2, TRUE),
    ('a6000000-0000-0000-0000-000000000004', 'Flipkart',                  'Flipkart', 'Private',  'E-Commerce',             'Bengaluru', 'Karnataka',   'India', 2007, 30000,  3.8, TRUE),
    ('a6000000-0000-0000-0000-000000000005', 'Tvarah',                    'Tvarah',   'Private',  'Information Technology', 'Pune',      'Maharashtra', 'India', 2023, 50,     5.0, TRUE);
--rollback DELETE FROM company WHERE id LIKE 'a6000000%';

-- ─────────────────────────────────────────────────────────────
-- job_title
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-7 labels:v0.0.1 context:dml
--comment: Sample data - job_title
INSERT INTO job_title (id, name, department, seniority_level) VALUES
    ('a7000000-0000-0000-0000-000000000001', 'Software Engineer',        'Backend Engineering',  'Mid'),
    ('a7000000-0000-0000-0000-000000000002', 'Senior Software Engineer', 'Backend Engineering',  'Senior'),
    ('a7000000-0000-0000-0000-000000000003', 'Tech Lead',                'Backend Engineering',  'Lead'),
    ('a7000000-0000-0000-0000-000000000004', 'Frontend Developer',       'Frontend Engineering', 'Mid'),
    ('a7000000-0000-0000-0000-000000000005', 'Data Engineer',            'Data Engineering',     'Mid'),
    ('a7000000-0000-0000-0000-000000000006', 'Product Manager',          'Product',              'Mid'),
    ('a7000000-0000-0000-0000-000000000007', 'HR Manager',               'Human Resources',      'Mid');
--rollback DELETE FROM job_title WHERE id LIKE 'a7000000%';

-- ─────────────────────────────────────────────────────────────
-- designation
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-8 labels:v0.0.1 context:dml
--comment: Sample data - designation
INSERT INTO designation (id, name, level, category, department, alias, is_active) VALUES
    ('a8000000-0000-0000-0000-000000000001', 'Software Engineer',        'L3', 'Individual Contributor', 'Backend Engineering',  'SDE',     TRUE),
    ('a8000000-0000-0000-0000-000000000002', 'Senior Software Engineer', 'L4', 'Individual Contributor', 'Backend Engineering',  'SDE II',  TRUE),
    ('a8000000-0000-0000-0000-000000000003', 'Tech Lead',                'L5', 'Lead',                   'Backend Engineering',  'TL',      TRUE),
    ('a8000000-0000-0000-0000-000000000004', 'Engineering Manager',      'L6', 'Manager',                'Engineering',          'EM',      TRUE),
    ('a8000000-0000-0000-0000-000000000005', 'Product Manager',          'L4', 'Individual Contributor', 'Product',              'PM',      TRUE),
    ('a8000000-0000-0000-0000-000000000006', 'HR Business Partner',      'L4', 'Individual Contributor', 'Human Resources',      'HRBP',    TRUE);
--rollback DELETE FROM designation WHERE id LIKE 'a8000000%';

-- ─────────────────────────────────────────────────────────────
-- skill
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-9 labels:v0.0.1 context:dml
--comment: Sample data - skill
INSERT INTO skill (id, name, category, tier, department, is_technical, is_verified) VALUES
    ('a9000000-0000-0000-0000-000000000001', 'Java',            'Programming Language', 'Core',     'Backend Engineering',  TRUE,  TRUE),
    ('a9000000-0000-0000-0000-000000000002', 'Spring Boot',     'Framework',            'Core',     'Backend Engineering',  TRUE,  TRUE),
    ('a9000000-0000-0000-0000-000000000003', 'PostgreSQL',      'Database',             'Core',     'Data Engineering',     TRUE,  TRUE),
    ('a9000000-0000-0000-0000-000000000004', 'React',           'Framework',            'Core',     'Frontend Engineering', TRUE,  TRUE),
    ('a9000000-0000-0000-0000-000000000005', 'Python',          'Programming Language', 'Core',     'Data Engineering',     TRUE,  TRUE),
    ('a9000000-0000-0000-0000-000000000006', 'AWS',             'Cloud',                'Advanced', 'Backend Engineering',  TRUE,  TRUE),
    ('a9000000-0000-0000-0000-000000000007', 'Communication',   'Soft Skill',           'General',  'Human Resources',      FALSE, TRUE),
    ('a9000000-0000-0000-0000-000000000008', 'Leadership',      'Soft Skill',           'General',  'Human Resources',      FALSE, TRUE);
--rollback DELETE FROM skill WHERE id LIKE 'a9000000%';

-- ─────────────────────────────────────────────────────────────
-- specialization_degree_map
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-10 labels:v0.0.1 context:dml
--comment: Sample data - specialization_degree_map
INSERT INTO specialization_degree_map (id, specialization_id, degree_id) VALUES
    ('aa000000-0000-0000-0000-000000000001', 'a4000000-0000-0000-0000-000000000001', 'a3000000-0000-0000-0000-000000000001'),
    ('aa000000-0000-0000-0000-000000000002', 'a4000000-0000-0000-0000-000000000001', 'a3000000-0000-0000-0000-000000000002'),
    ('aa000000-0000-0000-0000-000000000003', 'a4000000-0000-0000-0000-000000000002', 'a3000000-0000-0000-0000-000000000001'),
    ('aa000000-0000-0000-0000-000000000004', 'a4000000-0000-0000-0000-000000000004', 'a3000000-0000-0000-0000-000000000004'),
    ('aa000000-0000-0000-0000-000000000005', 'a4000000-0000-0000-0000-000000000005', 'a3000000-0000-0000-0000-000000000003');
--rollback DELETE FROM specialization_degree_map WHERE id LIKE 'aa000000%';

-- ─────────────────────────────────────────────────────────────
-- user
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-11 labels:v0.0.1 context:dml
--comment: Sample data - user
INSERT INTO "user" (id, first_name, last_name, email, role, status) VALUES
    ('ab000000-0000-0000-0000-000000000001', 'Rahul',   'Sharma',  'rahul.sharma@tvarah.com',  'RECRUITER', 'Active'),
    ('ab000000-0000-0000-0000-000000000002', 'Priya',   'Mehta',   'priya.mehta@tvarah.com',   'RECRUITER', 'Active'),
    ('ab000000-0000-0000-0000-000000000003', 'Vikram',  'Singh',   'vikram.singh@tvarah.com',  'ADMIN',     'Active'),
    ('ab000000-0000-0000-0000-000000000004', 'Sneha',   'Patil',   'sneha.patil@tvarah.com',   'RECRUITER', 'Inactive');
--rollback DELETE FROM "user" WHERE id LIKE 'ab000000%';

-- ─────────────────────────────────────────────────────────────
-- user_target
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-12 labels:v0.0.1 context:dml
--comment: Sample data - user_target
INSERT INTO user_target (id, user_id, positions_closed, sourcing, panel_interview, client_interview) VALUES
    ('ac000000-0000-0000-0000-000000000001', 'ab000000-0000-0000-0000-000000000001', 5, 30, 10, 5),
    ('ac000000-0000-0000-0000-000000000002', 'ab000000-0000-0000-0000-000000000002', 4, 25, 8,  4);
--rollback DELETE FROM user_target WHERE id LIKE 'ac000000%';

-- ─────────────────────────────────────────────────────────────
-- client
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-13 labels:v0.0.1 context:dml
--comment: Sample data - client
INSERT INTO client (id, name, industry, size, recruiter_id) VALUES
    ('ad000000-0000-0000-0000-000000000001', 'Acme Tech Solutions', 'Information Technology', 500,  'ab000000-0000-0000-0000-000000000001'),
    ('ad000000-0000-0000-0000-000000000002', 'FinServe India',      'Banking & Finance',      1200, 'ab000000-0000-0000-0000-000000000002'),
    ('ad000000-0000-0000-0000-000000000003', 'HealthPlus',          'Healthcare',             300,  'ab000000-0000-0000-0000-000000000001');
--rollback DELETE FROM client WHERE id LIKE 'ad000000%';

-- ─────────────────────────────────────────────────────────────
-- job_description
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-14 labels:v0.0.1 context:dml
--comment: Sample data - job_description
INSERT INTO job_description (id, client_id, job_title_id, job_description_text, experience_min_yrs, experience_max_yrs, required_skills, must_have_skills, salary_min, salary_max, total_positions, total_rounds, status, created_by) VALUES
    ('ae000000-0000-0000-0000-000000000001', 'ad000000-0000-0000-0000-000000000001', 'a7000000-0000-0000-0000-000000000002', 'We are looking for a Senior Software Engineer with strong Java and Spring Boot experience.', 3.0, 6.0, ARRAY['Java', 'Spring Boot', 'PostgreSQL'], ARRAY['Java', 'Spring Boot'], 1200000, 2000000, 3, 3, 'Open',  'rahul.sharma@tvarah.com'),
    ('ae000000-0000-0000-0000-000000000002', 'ad000000-0000-0000-0000-000000000002', 'a7000000-0000-0000-0000-000000000005', 'Looking for a Data Engineer with expertise in Python and cloud platforms.',                2.0, 5.0, ARRAY['Python', 'AWS', 'PostgreSQL'],   ARRAY['Python'],          1000000, 1800000, 2, 2, 'Open',  'priya.mehta@tvarah.com'),
    ('ae000000-0000-0000-0000-000000000003', 'ad000000-0000-0000-0000-000000000003', 'a7000000-0000-0000-0000-000000000006', 'Seeking a Product Manager to lead our healthcare product roadmap.',                        4.0, 8.0, ARRAY['Leadership', 'Communication'],   ARRAY['Leadership'],      1500000, 2500000, 1, 4, 'Draft', 'rahul.sharma@tvarah.com');
--rollback DELETE FROM job_description WHERE id LIKE 'ae000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-15 labels:v0.0.1 context:dml
--comment: Sample data - candidate
INSERT INTO candidate (id, first_name, last_name, email, address, city, country, phone_number, whatsapp_number, status, job_title_id, designation_id, current_company_id, total_experience, relevant_experience, resume_url, created_on, created_by) VALUES
    ('af000000-0000-0000-0000-000000000001', 'Amit',    'Kumar',   'amit.kumar@gmail.com',    '12 MG Road',    'Bengaluru', 'India', '9876543201', '9876543201', 'Active',     'a7000000-0000-0000-0000-000000000002', 'a8000000-0000-0000-0000-000000000002', 'a6000000-0000-0000-0000-000000000001', 5.0,  4.5,  'https://storage.tvarah.com/resumes/amit-kumar.pdf',    now(), 'rahul.sharma@tvarah.com'),
    ('af000000-0000-0000-0000-000000000002', 'Neha',    'Joshi',   'neha.joshi@gmail.com',    '45 Koregaon',   'Pune',      'India', '9876543202', '9876543202', 'Sourced',    'a7000000-0000-0000-0000-000000000005', 'a8000000-0000-0000-0000-000000000001', 'a6000000-0000-0000-0000-000000000002', 3.0,  3.0,  'https://storage.tvarah.com/resumes/neha-joshi.pdf',    now(), 'priya.mehta@tvarah.com'),
    ('af000000-0000-0000-0000-000000000003', 'Rohit',   'Verma',   'rohit.verma@gmail.com',   '7 Andheri',     'Mumbai',    'India', '9876543203', NULL,         'Active',     'a7000000-0000-0000-0000-000000000003', 'a8000000-0000-0000-0000-000000000003', 'a6000000-0000-0000-0000-000000000002', 7.0,  6.0,  'https://storage.tvarah.com/resumes/rohit-verma.pdf',   now(), 'rahul.sharma@tvarah.com'),
    ('af000000-0000-0000-0000-000000000004', 'Divya',   'Nair',    'divya.nair@gmail.com',    '3 Anna Nagar',  'Chennai',   'India', '9876543204', '9876543204', 'Backlog',    'a7000000-0000-0000-0000-000000000001', 'a8000000-0000-0000-0000-000000000001', 'a6000000-0000-0000-0000-000000000004', 2.0,  2.0,  'https://storage.tvarah.com/resumes/divya-nair.pdf',    now(), 'priya.mehta@tvarah.com'),
    ('af000000-0000-0000-0000-000000000005', 'Karan',   'Malhotra','karan.malhotra@gmail.com', '90 Sector 21', 'Noida',     'India', '9876543205', '9876543205', 'Converted',  'a7000000-0000-0000-0000-000000000002', 'a8000000-0000-0000-0000-000000000002', 'a6000000-0000-0000-0000-000000000003', 4.5,  4.0,  'https://storage.tvarah.com/resumes/karan-malhotra.pdf',now(), 'rahul.sharma@tvarah.com');
--rollback DELETE FROM candidate WHERE id LIKE 'af000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_preference
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-16 labels:v0.0.1 context:dml
--comment: Sample data - candidate_preference
INSERT INTO candidate_preference (id, candidate_id, current_location, primary_preferred_location, can_relocate, preferred_job_title_id, current_ctc, expected_ctc_min, expected_ctc_max, ctc_currency, work_mode_preference) VALUES
    ('b0000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'Bengaluru', 'Bengaluru', TRUE,  'a7000000-0000-0000-0000-000000000003', 1400000, 1800000, 2200000, 'INR', 'Hybrid'),
    ('b0000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000002', 'Pune',      'Bengaluru', TRUE,  'a7000000-0000-0000-0000-000000000005', 800000,  1000000, 1400000, 'INR', 'Remote'),
    ('b0000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000003', 'Mumbai',    'Bengaluru', FALSE, 'a7000000-0000-0000-0000-000000000003', 2000000, 2500000, 3200000, 'INR', 'Hybrid'),
    ('b0000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000004', 'Chennai',   'Pune',      TRUE,  'a7000000-0000-0000-0000-000000000002', 600000,  900000,  1200000, 'INR', 'On-site'),
    ('b0000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000005', 'Noida',     'Bengaluru', TRUE,  'a7000000-0000-0000-0000-000000000003', 1300000, 1600000, 2000000, 'INR', 'Hybrid');
--rollback DELETE FROM candidate_preference WHERE id LIKE 'b0000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_notice_period
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-17 labels:v0.0.1 context:dml
--comment: Sample data - candidate_notice_period
INSERT INTO candidate_notice_period (id, candidate_id, company_id, is_in_notice_period, notice_period_duration, resignation_date, earliest_joining_date, last_working_date) VALUES
    ('b1000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'a6000000-0000-0000-0000-000000000001', FALSE, 60,  NULL,                              now() + interval '7 days',  NULL),
    ('b1000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000002', 'a6000000-0000-0000-0000-000000000002', FALSE, 30,  NULL,                              now() + interval '30 days', NULL),
    ('b1000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000003', 'a6000000-0000-0000-0000-000000000002', TRUE,  90,  now() - interval '15 days',       now() + interval '75 days', now() + interval '75 days'),
    ('b1000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000004', 'a6000000-0000-0000-0000-000000000004', FALSE, 30,  NULL,                              now() + interval '14 days', NULL),
    ('b1000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000005', 'a6000000-0000-0000-0000-000000000003', FALSE, 0,   NULL,                              now() + interval '3 days',  NULL);
--rollback DELETE FROM candidate_notice_period WHERE id LIKE 'b1000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_score
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-18 labels:v0.0.1 context:dml
--comment: Sample data - candidate_score
INSERT INTO candidate_score (id, candidate_id, score, max_score, strength, feedback_comments, drop_flag, fraud_status) VALUES
    ('b2000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 82.5, 100, 'Strong',   'Good Java fundamentals, strong system design.',           FALSE, FALSE),
    ('b2000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000002', 74.0, 100, 'Moderate', 'Decent Python skills, needs improvement in cloud.',       FALSE, FALSE),
    ('b2000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000003', 91.0, 100, 'Strong',   'Exceptional leadership and architecture skills.',         FALSE, FALSE),
    ('b2000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000004', 60.5, 100, 'Weak',     'Limited experience, but good learning curve.',           FALSE, FALSE),
    ('b2000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000005', 78.0, 100, 'Moderate', 'Good overall fit, communication skills need improvement.', FALSE, FALSE);
--rollback DELETE FROM candidate_score WHERE id LIKE 'b2000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_score_category_wise
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-19 labels:v0.0.1 context:dml
--comment: Sample data - candidate_score_category_wise
INSERT INTO candidate_score_category_wise (id, candidate_score_id, label, score, max_score) VALUES
    ('b3000000-0000-0000-0000-000000000001', 'b2000000-0000-0000-0000-000000000001', 'Education Quality',   18.0, 25),
    ('b3000000-0000-0000-0000-000000000002', 'b2000000-0000-0000-0000-000000000001', 'Experience Quality',  22.0, 25),
    ('b3000000-0000-0000-0000-000000000003', 'b2000000-0000-0000-0000-000000000001', 'Skill Depth',         21.0, 25),
    ('b3000000-0000-0000-0000-000000000004', 'b2000000-0000-0000-0000-000000000001', 'Domain Depth',        21.5, 25),
    ('b3000000-0000-0000-0000-000000000005', 'b2000000-0000-0000-0000-000000000003', 'Education Quality',   22.0, 25),
    ('b3000000-0000-0000-0000-000000000006', 'b2000000-0000-0000-0000-000000000003', 'Experience Quality',  24.0, 25),
    ('b3000000-0000-0000-0000-000000000007', 'b2000000-0000-0000-0000-000000000003', 'Skill Depth',         23.0, 25),
    ('b3000000-0000-0000-0000-000000000008', 'b2000000-0000-0000-0000-000000000003', 'Domain Depth',        22.0, 25);
--rollback DELETE FROM candidate_score_category_wise WHERE id LIKE 'b3000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_resume_summary
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-20 labels:v0.0.1 context:dml
--comment: Sample data - candidate_resume_summary
INSERT INTO candidate_resume_summary (id, candidate_id, skill_depth, skill_depth_tags, experience_quality, experience_quality_tags, academic_background, academic_background_tags, domain_depth, domain_depth_tags) VALUES
    ('b4000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'High',   ARRAY['Java', 'Spring Boot', 'PostgreSQL'], 'High',   ARRAY['TCS', 'Product Dev'],    'Good', ARRAY['IIT', 'B.Tech CS'],  'High',   ARRAY['Backend', 'Microservices']),
    ('b4000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000002', 'Medium', ARRAY['Python', 'AWS'],                    'Medium', ARRAY['Infosys', 'Data'],       'Good', ARRAY['NIT', 'B.Tech IT'],  'Medium', ARRAY['Data Engineering']),
    ('b4000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000003', 'High',   ARRAY['Java', 'AWS', 'React'],             'High',   ARRAY['Infosys', 'Leadership'], 'Good', ARRAY['BITS', 'B.Tech CS'], 'High',   ARRAY['Architecture', 'Backend']);
--rollback DELETE FROM candidate_resume_summary WHERE id LIKE 'b4000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_experience
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-21 labels:v0.0.1 context:dml
--comment: Sample data - candidate_experience
INSERT INTO candidate_experience (id, candidate_id, company, designation, industry, job_title_id, start_date, end_date, is_current, experience_summary) VALUES
    ('b5000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'Tata Consultancy Services', 'Software Engineer',        'Information Technology', 'a7000000-0000-0000-0000-000000000001', '2019-07-01', '2021-12-31', FALSE, 'Worked on core banking microservices using Java and Spring Boot.'),
    ('b5000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000001', 'Infosys',                   'Senior Software Engineer', 'Information Technology', 'a7000000-0000-0000-0000-000000000002', '2022-01-01', NULL,         TRUE,  'Leading backend development for a fintech product.'),
    ('b5000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000002', 'Tata Consultancy Services', 'Software Engineer',        'Information Technology', 'a7000000-0000-0000-0000-000000000005', '2021-08-01', NULL,         TRUE,  'Building data pipelines using Python and Spark.'),
    ('b5000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000003', 'Infosys',                   'Software Engineer',        'Information Technology', 'a7000000-0000-0000-0000-000000000001', '2017-06-01', '2020-05-31', FALSE, 'Full stack development for enterprise applications.'),
    ('b5000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000003', 'Infosys',                   'Tech Lead',                'Information Technology', 'a7000000-0000-0000-0000-000000000003', '2020-06-01', NULL,         TRUE,  'Leading a team of 8 engineers on cloud migration projects.');
--rollback DELETE FROM candidate_experience WHERE id LIKE 'b5000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_education
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-22 labels:v0.0.1 context:dml
--comment: Sample data - candidate_education
INSERT INTO candidate_education (id, candidate_id, institution_id, degree_id, specialization_id, start_year, end_year, is_highest) VALUES
    ('b6000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'a5000000-0000-0000-0000-000000000004', 'a3000000-0000-0000-0000-000000000001', 'a4000000-0000-0000-0000-000000000001', 2015, 2019, TRUE),
    ('b6000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000002', 'a5000000-0000-0000-0000-000000000005', 'a3000000-0000-0000-0000-000000000001', 'a4000000-0000-0000-0000-000000000002', 2017, 2021, TRUE),
    ('b6000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000003', 'a5000000-0000-0000-0000-000000000003', 'a3000000-0000-0000-0000-000000000001', 'a4000000-0000-0000-0000-000000000001', 2013, 2017, FALSE),
    ('b6000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000003', 'a5000000-0000-0000-0000-000000000001', 'a3000000-0000-0000-0000-000000000003', 'a4000000-0000-0000-0000-000000000001', 2017, 2019, TRUE),
    ('b6000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000004', 'a5000000-0000-0000-0000-000000000005', 'a3000000-0000-0000-0000-000000000002', 'a4000000-0000-0000-0000-000000000002', 2019, 2023, TRUE);
--rollback DELETE FROM candidate_education WHERE id LIKE 'b6000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_skill
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-23 labels:v0.0.1 context:dml
--comment: Sample data - candidate_skill
INSERT INTO candidate_skill (id, candidate_id, skill_id, proficiency_level, years_experience, last_used_year, validation_status) VALUES
    ('b7000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'a9000000-0000-0000-0000-000000000001', 'Expert',        5.0, 2026, 'Verified'),
    ('b7000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000001', 'a9000000-0000-0000-0000-000000000002', 'Expert',        4.0, 2026, 'Verified'),
    ('b7000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000001', 'a9000000-0000-0000-0000-000000000006', 'Intermediate',  2.0, 2026, 'Pending'),
    ('b7000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000002', 'a9000000-0000-0000-0000-000000000005', 'Expert',        3.0, 2026, 'Verified'),
    ('b7000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000002', 'a9000000-0000-0000-0000-000000000003', 'Intermediate',  2.0, 2025, 'Verified'),
    ('b7000000-0000-0000-0000-000000000006', 'af000000-0000-0000-0000-000000000003', 'a9000000-0000-0000-0000-000000000001', 'Expert',        7.0, 2026, 'Verified'),
    ('b7000000-0000-0000-0000-000000000007', 'af000000-0000-0000-0000-000000000003', 'a9000000-0000-0000-0000-000000000006', 'Expert',        4.0, 2026, 'Verified'),
    ('b7000000-0000-0000-0000-000000000008', 'af000000-0000-0000-0000-000000000003', 'a9000000-0000-0000-0000-000000000008', 'Expert',        5.0, 2026, 'Verified');
--rollback DELETE FROM candidate_skill WHERE id LIKE 'b7000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_document
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-24 labels:v0.0.1 context:dml
--comment: Sample data - candidate_document
INSERT INTO candidate_document (id, candidate_id, type, url) VALUES
    ('b8000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'Resume',     'https://storage.tvarah.com/docs/amit-resume.pdf'),
    ('b8000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000001', 'Aadhar',     'https://storage.tvarah.com/docs/amit-aadhar.pdf'),
    ('b8000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000002', 'Resume',     'https://storage.tvarah.com/docs/neha-resume.pdf'),
    ('b8000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000003', 'Resume',     'https://storage.tvarah.com/docs/rohit-resume.pdf'),
    ('b8000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000003', 'Offer Letter','https://storage.tvarah.com/docs/rohit-offer.pdf');
--rollback DELETE FROM candidate_document WHERE id LIKE 'b8000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_social_media
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-25 labels:v0.0.1 context:dml
--comment: Sample data - candidate_social_media
INSERT INTO candidate_social_media (id, candidate_id, platform, url) VALUES
    ('b9000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'LinkedIn', 'https://linkedin.com/in/amit-kumar'),
    ('b9000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000001', 'GitHub',   'https://github.com/amit-kumar'),
    ('b9000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000002', 'LinkedIn', 'https://linkedin.com/in/neha-joshi'),
    ('b9000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000003', 'LinkedIn', 'https://linkedin.com/in/rohit-verma'),
    ('b9000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000003', 'GitHub',   'https://github.com/rohit-verma');
--rollback DELETE FROM candidate_social_media WHERE id LIKE 'b9000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_job
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-26 labels:v0.0.1 context:dml
--comment: Sample data - candidate_job
INSERT INTO candidate_job (id, candidate_id, job_id, is_active, status, created_by) VALUES
    ('ba000000-0000-0000-0000-000000000001', 'af000000-0000-0000-0000-000000000001', 'ae000000-0000-0000-0000-000000000001', TRUE,  'Active Internal', 'rahul.sharma@tvarah.com'),
    ('ba000000-0000-0000-0000-000000000002', 'af000000-0000-0000-0000-000000000002', 'ae000000-0000-0000-0000-000000000002', TRUE,  'Shortlisted',     'priya.mehta@tvarah.com'),
    ('ba000000-0000-0000-0000-000000000003', 'af000000-0000-0000-0000-000000000003', 'ae000000-0000-0000-0000-000000000001', TRUE,  'Potential',       'rahul.sharma@tvarah.com'),
    ('ba000000-0000-0000-0000-000000000004', 'af000000-0000-0000-0000-000000000004', 'ae000000-0000-0000-0000-000000000001', FALSE, 'Backlog',         'priya.mehta@tvarah.com'),
    ('ba000000-0000-0000-0000-000000000005', 'af000000-0000-0000-0000-000000000005', 'ae000000-0000-0000-0000-000000000001', FALSE, 'Joined',          'rahul.sharma@tvarah.com');
--rollback DELETE FROM candidate_job WHERE id LIKE 'ba000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_job_evaluation
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-27 labels:v0.0.1 context:dml
--comment: Sample data - candidate_job_evaluation
INSERT INTO candidate_job_evaluation (id, candidate_job_id, jd_overall_match_score, skill_match_score, domain_match_score, jd_exp_relevance_score, status) VALUES
    ('bb000000-0000-0000-0000-000000000001', 'ba000000-0000-0000-0000-000000000001', 85.0, 88.0, 82.0, 84.0, 'Strong Match'),
    ('bb000000-0000-0000-0000-000000000002', 'ba000000-0000-0000-0000-000000000002', 73.0, 75.0, 70.0, 74.0, 'Moderate Match'),
    ('bb000000-0000-0000-0000-000000000003', 'ba000000-0000-0000-0000-000000000003', 92.0, 90.0, 94.0, 91.0, 'Strong Match'),
    ('bb000000-0000-0000-0000-000000000004', 'ba000000-0000-0000-0000-000000000004', 58.0, 55.0, 60.0, 59.0, 'Weak Match'),
    ('bb000000-0000-0000-0000-000000000005', 'ba000000-0000-0000-0000-000000000005', 80.0, 78.0, 82.0, 80.0, 'Strong Match');
--rollback DELETE FROM candidate_job_evaluation WHERE id LIKE 'bb000000%';

-- ─────────────────────────────────────────────────────────────
-- candidate_job_interview
-- ─────────────────────────────────────────────────────────────
--changeset tvarah:036-28 labels:v0.0.1 context:dml
--comment: Sample data - candidate_job_interview
INSERT INTO candidate_job_interview (id, candidate_job_id, round_number, interviewer_name, interviewer_email, interviewer_type, scheduled_on, mode, status, feedback, score) VALUES
    ('bc000000-0000-0000-0000-000000000001', 'ba000000-0000-0000-0000-000000000001', 1, 'Rahul Sharma',  'rahul.sharma@tvarah.com',   'Internal', now() + interval '2 days',  'Video',     'Scheduled',  NULL,                            NULL),
    ('bc000000-0000-0000-0000-000000000002', 'ba000000-0000-0000-0000-000000000003', 1, 'Priya Mehta',   'priya.mehta@tvarah.com',    'Internal', now() - interval '5 days',  'Video',     'Completed',  'Excellent technical skills.',   92.0),
    ('bc000000-0000-0000-0000-000000000003', 'ba000000-0000-0000-0000-000000000003', 2, 'Client Panel',  'panel@acmetech.com',         'Client',   now() + interval '3 days',  'In-person', 'Scheduled',  NULL,                            NULL),
    ('bc000000-0000-0000-0000-000000000004', 'ba000000-0000-0000-0000-000000000005', 1, 'Vikram Singh',  'vikram.singh@tvarah.com',   'Internal', now() - interval '30 days', 'Video',     'Completed',  'Strong candidate, good fit.',   85.0),
    ('bc000000-0000-0000-0000-000000000005', 'ba000000-0000-0000-0000-000000000005', 2, 'Client HR',     'hr@acmetech.com',            'Client',   now() - interval '20 days', 'In-person', 'Completed',  'Offer extended and accepted.',  88.0);
--rollback DELETE FROM candidate_job_interview WHERE id LIKE 'bc000000%';
