package net.datasa.firstproject.exception;

/**
 * InvalidPhoneNumberException
 * - 휴대전화번호 형식이 올바르지 않을 때 발생하는 예외입니다.
 * - 규칙: 10~11자, 숫자만 가능
 */
public class InvalidPhoneNumberException extends ValidationException {

    public InvalidPhoneNumberException() {
        super("휴대전화번호는 10~11자, 숫자만 가능합니다.");
    }
}
