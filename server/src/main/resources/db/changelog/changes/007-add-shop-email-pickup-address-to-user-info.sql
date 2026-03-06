-- liquibase formatted sql
-- changeset sangnt:007-add-shop-email-pickup-address-to-user-info

ALTER TABLE user_info ADD COLUMN IF NOT EXISTS shop_email VARCHAR(100) DEFAULT NULL;
ALTER TABLE user_info ADD COLUMN IF NOT EXISTS pickup_address VARCHAR(255) DEFAULT NULL;
