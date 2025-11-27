-- 모든 데이터베이스 조회
SHOW DATABASES;

-- 새 데이터베이스 생성(예: 48_backend)
CREATE DATABASE `48_backend`;

-- 특정 데이터베이스 사용 선언(예: 48_backend)
USE `48_backend`;

-- 해당 데이터베이스의 모든 테이블 조회
SHOW TABLES;

-- 사용자(project_user) 테이블 생성
CREATE TABLE project_user
(
    user_id          VARCHAR(30) PRIMARY KEY NOT NULL UNIQUE,
    user_name        VARCHAR(50)             NOT NULL,
    password         VARCHAR(50)             NOT NULL,
    confirm_password VARCHAR(50)             NOT NULL,
    phone_number     VARCHAR(20)             NOT NULL
);

-- 해당 테이블이 정상적으로 작성되었는지를 확인
SHOW TABLES;

-- 임시 사용자 데이터 추가
INSERT INTO project_user
VALUES ('kamillee0918', '이민재', 'kamillee_!mportpassword', 'kamillee_!mportpassword', '010-1234-1234');

-- 사용자(project_user) 테이블 조회
SELECT *
FROM project_user;