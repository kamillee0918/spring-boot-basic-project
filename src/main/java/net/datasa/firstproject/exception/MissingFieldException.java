package net.datasa.firstproject.exception;

/**
 * MissingFieldException
 * - 필수 입력 필드가 누락되었을 때 발생하는 예외입니다.
 */
public class MissingFieldException extends ValidationException {

    public MissingFieldException(String fieldName) {
        super(fieldName + " 입력해 주세요.");
    }

    public MissingFieldException(String message, boolean isCustomMessage) {
        super(message);
    }
}
