package net.datasa.firstproject.exception;

/**
 * InvalidUserNameException
 * - 사용자 이름 형식이 올바르지 않을 때 발생하는 예외입니다.
 * - 규칙: 2~20자, 한글만 가능
 */
public class InvalidUserNameException extends ValidationException {

    public InvalidUserNameException() {
        super("이름은 2~20자, 한글만 가능합니다.");
    }

    public InvalidUserNameException(String message) {
        super(message);
    }
}
