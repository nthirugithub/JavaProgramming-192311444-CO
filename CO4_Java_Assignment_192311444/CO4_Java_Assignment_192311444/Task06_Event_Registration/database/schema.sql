CREATE DATABASE IF NOT EXISTS campus_events CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_events;
CREATE TABLE IF NOT EXISTS events (
 event_id INT AUTO_INCREMENT PRIMARY KEY,
 event_name VARCHAR(120) NOT NULL UNIQUE,
 event_date DATE NOT NULL,
 venue VARCHAR(120) NOT NULL,
 capacity INT NOT NULL CHECK(capacity>0),
 active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS registrations (
 registration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 student_reg_no VARCHAR(20) NOT NULL,
 student_name VARCHAR(120) NOT NULL,
 event_id INT NOT NULL,
 email VARCHAR(160) NOT NULL,
 registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_registration_event FOREIGN KEY(event_id) REFERENCES events(event_id),
 CONSTRAINT uq_student_event UNIQUE(student_reg_no,event_id),
 INDEX idx_registration_event_date(event_id,registered_at)
);
INSERT INTO events(event_name,event_date,venue,capacity) VALUES
('Tech Symposium 2026','2026-12-05','Main Auditorium',300),
('Cultural Fest 2026','2026-12-12','Open Air Theatre',500),
('Project Expo 2026','2026-12-18','Block B Exhibition Hall',200)
ON DUPLICATE KEY UPDATE event_name=VALUES(event_name);
