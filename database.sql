
-- database
CREATE DATABASE IF NOT EXISTS auth_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE auth_db;

-- users table
CREATE TABLE IF NOT EXISTS users (
  id          INT           NOT NULL AUTO_INCREMENT,
  first_name  VARCHAR(50)   NOT NULL,
  last_name   VARCHAR(50)   NOT NULL,
  email       VARCHAR(255)  NOT NULL,
  password    VARCHAR(255)  NOT NULL,   
  created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE KEY uq_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
