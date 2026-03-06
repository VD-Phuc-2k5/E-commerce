-- liquibase formatted sql
-- changeset sangnt:006-add-missing-type-user-address
ALTER TABLE user_addresses 
    DROP COLUMN type,
    ADD COLUMN type VARCHAR(20) CHECK(type IN ('HOME', 'WORK', 'OTHER', 'PICKUP')) DEFAULT 'HOME';
