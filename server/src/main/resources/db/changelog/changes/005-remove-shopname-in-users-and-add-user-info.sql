-- liquibase formatted sql
-- changeset sangnt:005-remove-shopname-in-users-and-add-user-info
ALTER TABLE users DROP COLUMN shop_name;
ALTER TABLE users DROP COLUMN user_address;

CREATE TABLE user_addresses (
    address_id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    address VARCHAR(255) NOT NULL,
    type VARCHAR(20) CHECK(type IN ('HOME', 'WORK', 'OTHER')) DEFAULT 'HOME',
    is_default BOOLEAN DEFAULT FALSE,
    position_map VARCHAR(255) DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE user_info (
    user_id UUID PRIMARY KEY,
    phone VARCHAR(15) DEFAULT NULL,
    -- seller only
    shop_name VARCHAR(100) DEFAULT NULL,
    shop_description TEXT DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE TABLE user_bank_accounts (
    bank_account_id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    account_holder VARCHAR(100) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
