package org.sopt.exception.error;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.ErrorEnum;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UnauthorizedError implements ErrorEnum {

    // auth
    UNSUPPORTED_PLATFORM("Unsupported Authenticate Platform."),

    // auth (token)
    INVALID_ACCESS_TOKEN("Access Token is invalid."),
    EXPIRED_ACCESS_TOKEN("Access Token is expired."),
    EXPIRED_REFRESH_TOKEN("Refresh Token is expired."),

    ;

    private final HttpStatus status = HttpStatus.UNAUTHORIZED;
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
