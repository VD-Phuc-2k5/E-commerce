-- liquibase formatted sql
-- changeset sangnt:004-insert-seed-data-role
INSERT INTO roles (role_name, role_description) VALUES
  ('SELLER', 'Người bán'),
  ('BUYER', 'Người mua'),
  ('ADMIN', 'Quản trị viên'),
  ('GUEST', 'Khách vãng lai')
ON CONFLICT (role_name) DO NOTHING
