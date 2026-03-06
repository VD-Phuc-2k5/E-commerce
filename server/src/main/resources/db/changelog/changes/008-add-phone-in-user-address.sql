-- liquibase formatted sql
-- changeset sangnt:008-add-phone-in-user-address

ALTER TABLE user_addresses ADD COLUMN IF NOT EXISTS phone VARCHAR(20) DEFAULT NULL;
