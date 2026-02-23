-- liquibase formatted sql 
-- changeset sangnt:003-fix-gender-and-seed-roles
-- comment: Fix user_gender to store actual gender (M/F/O), seed default roles, assign GUEST role on register

-- 1. Fix user_gender: drop old CHECK (SELLER/ADMIN/...), add correct gender CHECK (M/F/O)
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_user_gender_check;
ALTER TABLE users ALTER COLUMN user_gender SET DEFAULT NULL;
ALTER TABLE users ADD CONSTRAINT users_user_gender_check CHECK(user_gender IN ('M', 'F', 'O'));
-- Reset existing bad data (GUEST was default) to NULL
UPDATE users SET user_gender = NULL WHERE user_gender IN ('SELLER', 'ADMIN', 'GUEST', 'BUYER');

-- 2. Seed default roles
INSERT INTO roles (role_name, role_description) VALUES
    ('GUEST', 'Default role for new users'),
    ('BUYER', 'Buyer role'),
    ('SELLER', 'Seller role'),
    ('ADMIN', 'Administrator role')
ON CONFLICT (role_name) DO NOTHING;
