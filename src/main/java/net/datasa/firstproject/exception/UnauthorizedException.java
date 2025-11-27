package net.datasa.firstproject.exception;

/**
 * UnauthorizedException
 * - 세션에 userId가 없을 때 발생하는 예외입니다.
 */
public class UnauthorizedException extends ValidationException {

    public UnauthorizedException() {
        super("로그인이 필요한 서비스입니다.");
    }
}
