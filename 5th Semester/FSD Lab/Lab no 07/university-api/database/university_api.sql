-- =====================================================
--  University API Access Management System
--  Import in phpMyAdmin (Import tab) or run in MySQL
-- =====================================================
CREATE DATABASE IF NOT EXISTS university_api
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE university_api;

DROP TABLE IF EXISTS api_logs;
DROP TABLE IF EXISTS api_keys;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS students;

CREATE TABLE students (
  id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  roll_no     VARCHAR(20)  NOT NULL UNIQUE,
  name        VARCHAR(100) NOT NULL,
  email       VARCHAR(120) NOT NULL UNIQUE,
  department  VARCHAR(80)  NOT NULL,
  semester    TINYINT UNSIGNED NOT NULL
) ENGINE=InnoDB;

CREATE TABLE teachers (
  id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  email       VARCHAR(120) NOT NULL UNIQUE,
  department  VARCHAR(80)  NOT NULL
) ENGINE=InnoDB;

CREATE TABLE courses (
  id      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  code    VARCHAR(20)  NOT NULL UNIQUE,
  title   VARCHAR(120) NOT NULL
) ENGINE=InnoDB;

-- assignment (max 20) + midterm (max 30) + final_exam (max 50) = total (max 100)
CREATE TABLE marks (
  id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  student_id  INT UNSIGNED NOT NULL,
  course_id   INT UNSIGNED NOT NULL,
  assignment  DECIMAL(5,2) NOT NULL DEFAULT 0,
  midterm     DECIMAL(5,2) NOT NULL DEFAULT 0,
  final_exam  DECIMAL(5,2) NOT NULL DEFAULT 0,
  UNIQUE KEY uq_student_course (student_id, course_id),
  CONSTRAINT fk_marks_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  CONSTRAINT fk_marks_course  FOREIGN KEY (course_id)  REFERENCES courses(id)  ON DELETE CASCADE
) ENGINE=InnoDB;

-- A key belongs to exactly one student OR one teacher
CREATE TABLE api_keys (
  id             INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  api_key        VARCHAR(64)  NOT NULL UNIQUE,
  owner_type     ENUM('student','teacher') NOT NULL,
  student_id     INT UNSIGNED NULL,
  teacher_id     INT UNSIGNED NULL,
  request_limit  INT UNSIGNED NOT NULL DEFAULT 20,
  requests_used  INT UNSIGNED NOT NULL DEFAULT 0,
  status         ENUM('active','revoked') NOT NULL DEFAULT 'active',
  created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_used_at   TIMESTAMP NULL DEFAULT NULL,
  revoked_at     TIMESTAMP NULL DEFAULT NULL,
  CONSTRAINT fk_key_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  CONSTRAINT fk_key_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- One row per request made with a key (usage history)
CREATE TABLE api_logs (
  id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  api_key_id   INT UNSIGNED NOT NULL,
  method       VARCHAR(10)  NOT NULL,
  endpoint     VARCHAR(200) NOT NULL,
  status_code  SMALLINT UNSIGNED NOT NULL,
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_logs_key (api_key_id),
  CONSTRAINT fk_log_key FOREIGN KEY (api_key_id) REFERENCES api_keys(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------- Sample data ----------------
INSERT INTO students (roll_no, name, email, department, semester) VALUES
  ('245187', 'Wasif Ali',       'wasif@student.au.edu.pk',  'Computer Science', 5),
  ('245101', 'Ayesha Khan',     'ayesha@student.au.edu.pk', 'Computer Science', 5),
  ('245102', 'Hamza Tariq',     'hamza@student.au.edu.pk',  'Computer Science', 5),
  ('245103', 'Fatima Noor',     'fatima@student.au.edu.pk', 'Computer Science', 5),
  ('245104', 'Bilal Ahmed',     'bilal@student.au.edu.pk',  'Computer Science', 5),
  ('245105', 'Zainab Siddiqui', 'zainab@student.au.edu.pk', 'Computer Science', 5);

INSERT INTO teachers (name, email, department) VALUES
  ('M. Fahad Qasim', 'fahad.qasim@au.edu.pk', 'Computer Science'),
  ('Sara Malik',     'sara.malik@au.edu.pk',  'Computer Science');

INSERT INTO courses (code, title) VALUES
  ('CS-301L', 'Full Stack Development Lab'),
  ('CS-302',  'Database Systems'),
  ('CS-303',  'Operating Systems'),
  ('CS-304',  'Software Engineering');

INSERT INTO marks (student_id, course_id, assignment, midterm, final_exam) VALUES
  (1,1,13.9,18.1,42.3),
  (1,2,11.7,23.5,36.0),
  (1,3,11.5,23.1,28.8),
  (1,4,14.9,17.0,30.0),
  (2,1,14.8,27.6,30.7),
  (2,2,13.0,24.8,48.8),
  (2,3,16.2,21.6,49.5),
  (2,4,11.4,28.0,34.4),
  (3,1,12.3,17.6,34.8),
  (3,2,18.3,18.5,40.8),
  (3,3,16.8,21.2,40.1),
  (3,4,11.6,16.8,32.5),
  (4,1,17.1,22.0,34.9),
  (4,2,16.3,22.3,34.6),
  (4,3,18.1,25.8,33.4),
  (4,4,16.2,23.4,47.3),
  (5,1,17.6,20.0,49.6),
  (5,2,12.1,21.9,44.7),
  (5,3,12.4,22.8,28.9),
  (5,4,17.0,26.7,40.6),
  (6,1,18.9,20.4,43.3),
  (6,2,16.3,24.1,38.0),
  (6,3,18.6,29.2,38.4),
  (6,4,17.0,16.8,43.4);
