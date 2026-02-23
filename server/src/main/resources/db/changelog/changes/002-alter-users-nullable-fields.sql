-- liquibase formatted sql 
-- changeset sangnt:002-alter-users-nullable-fields
-- comment: Allow null for user_password and name to support OTP-first signup flow
ALTER TABLE users ALTER COLUMN user_password DROP NOT NULL;
ALTER TABLE users ALTER COLUMN name DROP NOT NULL;
ALTER TABLE users ALTER COLUMN name SET DEFAULT '';
