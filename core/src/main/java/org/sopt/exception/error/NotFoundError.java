package org.sopt.exception.error;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.ErrorEnum;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NotFoundError implements ErrorEnum {

    // auth
    REGISTRATION_PLATFORM_NOT_FOUND("Can not found in Support Registration Platforms."),

    // auth (token)
    REFRESH_TOKEN_NOT_FOUND("Refresh Token not found."),

    // user
    USER_NOT_FOUND("Can not found user."),
    ;
    private final HttpStatus status = HttpStatus.NOT_FOUND;
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
