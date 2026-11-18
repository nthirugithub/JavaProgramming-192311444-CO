CREATE DATABASE IF NOT EXISTS co5_bus_pass CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE co5_bus_pass;

CREATE TABLE IF NOT EXISTS bus_pass (
    pass_id VARCHAR(30) PRIMARY KEY,
    reg_no VARCHAR(30) NOT NULL,
    name VARCHAR(120) NOT NULL,
    route_no INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    INDEX idx_bus_pass_route (route_no),
    INDEX idx_bus_pass_reg (reg_no)
);

CREATE TABLE IF NOT EXISTS student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    register_no VARCHAR(30) NOT NULL UNIQUE,
    student_name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(80) PRIMARY KEY,
    pwd VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS wallet (
    wallet_id BIGINT PRIMARY KEY,
    balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
);
