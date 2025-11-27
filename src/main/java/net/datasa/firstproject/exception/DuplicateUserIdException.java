package net.datasa.firstproject.exception;

/**
 * DuplicateUserIdException
 * - 이미 사용 중인 아이디로 회원가입 시도 시 발생하는 예외입니다.
 */
public class DuplicateUserIdException extends ValidationException {

    public DuplicateUserIdException() {
        super("이미 사용 중인 아이디입니다.");
    }
}
