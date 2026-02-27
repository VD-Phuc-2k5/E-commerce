-- liquibase formatted sql 
-- changeset sangnt:001-init-schema-user
-- comment: Initial schema for signup user table
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE,
    name VARCHAR(30) NOT NULL,
    user_email VARCHAR(100) UNIQUE,
    user_phone VARCHAR(15) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_avatar VARCHAR(255) DEFAULT '',
    user_gender VARCHAR(10) CHECK(user_gender IN ('SELLER', 'ADMIN', 'GUEST', 'BUYER')) DEFAULT 'GUEST',
    user_status VARCHAR(10) CHECK(user_status IN ('ACTIVE', 'INACTIVE', 'BANNED')) DEFAULT 'ACTIVE',
    user_dob DATE, 
    user_address VARCHAR(255) DEFAULT '',
    phone_verified BOOLEAN DEFAULT FALSE,
    email_verified BOOLEAN DEFAULT FALSE,
    kyc_status VARCHAR(10) CHECK(kyc_status IN ('UNVERIFIED', 'VERIFIED', 'PENDING')) DEFAULT 'UNVERIFIED',
    shop_name VARCHAR(100) DEFAULT '',
    id_card_number VARCHAR(20) DEFAULT '',
    id_card_front_url VARCHAR(255) DEFAULT '',
    id_card_back_url VARCHAR(255) DEFAULT ''
);

CREATE TABLE roles (
    role_name VARCHAR(20) PRIMARY KEY,
    role_description VARCHAR(100) DEFAULT ''
);

CREATE TABLE user_roles (
    id_seq SERIAL PRIMARY KEY,
    user_id UUID,
    role_name VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_name) REFERENCES roles(role_name) ON DELETE CASCADE
);

CREATE TABLE permissions (
    permission_name VARCHAR(50) PRIMARY KEY,
    permission_description VARCHAR(100) DEFAULT ''
);

CREATE TABLE role_permissions (
    id_seq SERIAL PRIMARY KEY,
    role_name VARCHAR(20),
    permission_name VARCHAR(50),
    FOREIGN KEY (role_name) REFERENCES roles(role_name) ON DELETE CASCADE,
    FOREIGN KEY (permission_name) REFERENCES permissions(permission_name) ON DELETE CASCADE
);
