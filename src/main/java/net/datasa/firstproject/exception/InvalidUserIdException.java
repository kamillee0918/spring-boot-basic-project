package net.datasa.firstproject.exception;

/**
 * InvalidUserIdException
 * - 사용자 아이디 형식이 올바르지 않을 때 발생하는 예외입니다.
 * - 규칙: 3~14자, 영문/숫자/특수문자만 가능
 */
public class InvalidUserIdException extends ValidationException {

    public InvalidUserIdException() {
        super("아이디는 3~14자, 영문/숫자/특수문자만 가능합니다.");
    }

    public InvalidUserIdException(String message) {
        super(message);
    }
}
