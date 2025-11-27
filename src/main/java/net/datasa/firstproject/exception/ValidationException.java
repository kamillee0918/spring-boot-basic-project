package net.datasa.firstproject.exception;

/**
 * ValidationException
 * - 회원 정보 유효성 검증 실패 시 발생하는 예외의 최상위 클래스입니다.
 * - 모든 구체적인 유효성 검증 예외는 이 클래스를 상속받습니다.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
