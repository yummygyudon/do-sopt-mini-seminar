package org.sopt.exception.error;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.ErrorEnum;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum InternalServerError implements ErrorEnum {

    INTERNAL_DATABASE_ERROR("내부 데이터베이스에서 에러가 발생했습니다."),
    INTERNAL_SERVER_ERROR("알 수 없는 서버 에러가 발생했습니다."),
    INTERNAL_SERVER_SESSION_ERROR("Session Error"),


    PARSE_ERROR("Exception caused by Parsing"),
    RESOLVER_EXCEPTION("Exception caused by Resolver Method."),
    ;

    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String errorMessage;

    @Override
    public int getStatusCode() {
        return status.value();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
