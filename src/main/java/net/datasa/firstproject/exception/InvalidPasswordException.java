package net.datasa.firstproject.exception;

/**
 * InvalidPasswordException
 * - 비밀번호 형식이 올바르지 않을 때 발생하는 예외입니다.
 * - 규칙: 8~14자, 영문/숫자/특수문자만 가능
 */
public class InvalidPasswordException extends ValidationException {

    public InvalidPasswordException() {
        super("비밀번호는 8~14자, 영문/숫자/특수문자만 가능합니다.");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
