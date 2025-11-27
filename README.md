# 🚀 Spring Boot Member Management System

Spring Boot 기반의 세션 인증 회원 관리 시스템입니다.

## 📋 프로젝트 개요

이 프로젝트는 **Spring Boot 3.5.8**과 **Java 21**을 기반으로 구축된 회원 관리 웹 애플리케이션입니다. Spring Security를 사용하지 않고 순수 HttpSession을 활용하여 인증
시스템을 구현했으며, 유효성 검증과 예외 처리 구조를 갖추고 있습니다.

### 🎯 핵심 목표

- **📚 학습용 프로젝트**: Spring Boot 기본기 학습 및 MVC 패턴 이해
- **🔐 순수 세션 인증**: Spring Security 없이 HttpSession 기반 인증 구현
- **✅ 체계적 검증**: 사용자 정의 예외를 활용한 명확한 유효성 검증
- **🧪 테스트 주도**: 단위/통합 테스트를 통한 안정성 확보
- **🏗️ 클린 아키텍처**: Controller-Service-Repository 계층 분리

### ✨ 구현 완료 기능

#### 1️⃣ 회원가입 시스템

- **정규식 기반 유효성 검증**
    - 아이디: 3~14자, 영문/숫자/특수문자
    - 비밀번호: 8~14자, 영문/숫자/특수문자
    - 이름: 2~20자, 한글만 허용
    - 전화번호: 10~11자, 숫자만 (자동 포맷팅: 010-1234-5678)
- **중복 검사**: 아이디 중복 확인
- **비밀번호 확인**: 비밀번호 일치 여부 검증
- **사용자 친화적 에러**: 구체적인 예외 메시지 제공

#### 2️⃣ 로그인/로그아웃

- **세션 기반 인증**: HttpSession을 활용한 상태 관리
- **인증 유지**: userId, userName을 세션에 저장
- **세션 타임아웃**: 1시간 자동 만료
- **안전한 로그아웃**: 세션 정보 완전 제거

#### 3️⃣ 접근 제어

- **보호된 리소스**: 로그인 사용자만 접근 가능한 페이지
- **자동 리디렉션**: 미인증 시 로그인 페이지로 이동
- **사용자 목록**: 전체 회원 정보 조회 (로그인 필수)

#### 4️⃣ 예외 처리 체계

- **계층적 예외 구조**: ValidationException을 상속한 7개의 구체적 예외
  ```
  ValidationException (부모)
  ├─ InvalidUserIdException
  ├─ InvalidPasswordException
  ├─ PasswordMismatchException
  ├─ InvalidUserNameException
  ├─ InvalidPhoneNumberException
  ├─ DuplicateUserIdException
  └─ MissingFieldException
  ```
- **명확한 에러 메시지**: 각 예외마다 사용자 친화적인 메시지
- **타입 안전성**: 예외 타입으로 오류 구분

#### 5️⃣ 테스트 커버리지

- **총 26개 테스트 (100% 통과 ✅)**
    - MemberServiceTest: 19개 (비즈니스 로직 검증)
    - MemberControllerTest: 7개 (웹 계층 검증)
- **높은 커버리지**
    - MemberService: 76% Line, 77% Branch
    - MemberController: 73% Line
- **Mock 기반 테스트**: Mockito를 활용한 독립적 단위 테스트

### 🎨 사용자 인터페이스

- **Thymeleaf 템플릿**: 서버 사이드 렌더링
- **반응형 디자인**: 모바일/데스크탑 대응
- **직관적인 폼**: 입력값 유지 및 에러 표시
- **세션 상태 표시**: 로그인 사용자명 환영 메시지

### 🔄 데이터 흐름

## 📊 데이터베이스 스키마

### 🗄️ project_user 테이블

회원 정보를 저장하는 메인 테이블입니다. 현재는 학습 목적으로 `confirm_password`를 포함하고 있습니다.

| 필드               | 타입          | 제약조건                  | 설명                          |
|------------------|-------------|-----------------------|-----------------------------|
| user_id          | VARCHAR(30) | PRIMARY KEY, NOT NULL | 사용자 아이디 (3~14자, 영문/숫자/특수문자) |
| user_name        | VARCHAR(50) | NOT NULL              | 사용자 이름 (2~20자, 한글만)         |
| password         | VARCHAR(50) | NOT NULL              | 비밀번호 (8~14자, 평문 저장*)        |
| confirm_password | VARCHAR(50) | NOT NULL              | 비밀번호 확인 (동일 값)              |
| phone_number     | VARCHAR(20) | NOT NULL              | 전화번호 (010-1234-5678 형식)     |

> **⚠️ 보안 참고사항**
> - 현재 비밀번호는 **평문으로 저장**됩니다 (학습 목적)
> - 실무 환경에서는 반드시 **BCrypt** 등의 해시 알고리즘 사용 필요
> - `confirm_password` 컬럼은 추후 제거 예정 (클라이언트 검증으로 충분)

#### 제약조건 및 인덱스

- **PRIMARY KEY**: `user_id` (유일 식별자)
- **UNIQUE**: `user_id` (중복 불가)
- **NOT NULL**: 모든 필드 (필수 입력)
- **인덱스**: `user_id` (조회 성능 최적화)

## 👤 작성자

**Kamil Lee**

- GitHub: [@kamillee0918](https://github.com/kamillee0918)
