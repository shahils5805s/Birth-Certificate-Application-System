-- Birth Certificate Application System — Oracle Schema
-- Run as your application schema user (e.g. SYSTEM or a dedicated user)

-- Drop existing (optional, for clean re-runs)
BEGIN EXECUTE IMMEDIATE 'DROP TABLE certificates CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE applications CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE users CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE user_seq'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE app_seq'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE cert_seq'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- Sequences
CREATE SEQUENCE user_seq START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE app_seq  START WITH 5000 INCREMENT BY 1;
CREATE SEQUENCE cert_seq START WITH 9000 INCREMENT BY 1;

-- Users table (both citizens and admins)
CREATE TABLE users (
    user_id       NUMBER PRIMARY KEY,
    username      VARCHAR2(50) UNIQUE NOT NULL,
    password      VARCHAR2(100) NOT NULL,
    full_name     VARCHAR2(100) NOT NULL,
    email         VARCHAR2(100),
    phone         VARCHAR2(15),
    role          VARCHAR2(10) NOT NULL CHECK (role IN ('CITIZEN','ADMIN')),
    created_at    TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- Applications table
CREATE TABLE applications (
    application_id     NUMBER PRIMARY KEY,
    user_id            NUMBER NOT NULL,
    child_name         VARCHAR2(100) NOT NULL,
    gender             VARCHAR2(10) NOT NULL,
    date_of_birth      DATE NOT NULL,
    place_of_birth     VARCHAR2(150) NOT NULL,
    father_name        VARCHAR2(100) NOT NULL,
    mother_name        VARCHAR2(100) NOT NULL,
    address            VARCHAR2(300) NOT NULL,
    status             VARCHAR2(15) DEFAULT 'PENDING'
                          CHECK (status IN ('PENDING','APPROVED','REJECTED')),
    submitted_at       TIMESTAMP DEFAULT SYSTIMESTAMP,
    remarks            VARCHAR2(300),
    CONSTRAINT fk_app_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Certificates table (issued on approval)
CREATE TABLE certificates (
    certificate_id    NUMBER PRIMARY KEY,
    certificate_no    VARCHAR2(30) UNIQUE NOT NULL,
    application_id    NUMBER UNIQUE NOT NULL,
    issued_by         NUMBER NOT NULL,
    issued_at         TIMESTAMP DEFAULT SYSTIMESTAMP,
    CONSTRAINT fk_cert_app  FOREIGN KEY (application_id) REFERENCES applications(application_id),
    CONSTRAINT fk_cert_admn FOREIGN KEY (issued_by) REFERENCES users(user_id)
);

-- Seed default admin (username: admin / password: admin123)
INSERT INTO users (user_id, username, password, full_name, email, phone, role)
VALUES (user_seq.NEXTVAL, 'admin', 'admin123', 'System Administrator',
        'admin@bcas.gov', '9999999999', 'ADMIN');

COMMIT;
