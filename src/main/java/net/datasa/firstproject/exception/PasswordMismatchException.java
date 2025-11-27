package net.datasa.firstproject.exception;

/**
 * PasswordMismatchException
 * - 비밀번호와 비밀번호 확인이 일치하지 않을 때 발생하는 예외입니다.
 */
public class PasswordMismatchException extends ValidationException {

    public PasswordMismatchException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
