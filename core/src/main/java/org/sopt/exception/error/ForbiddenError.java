package org.sopt.exception.error;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.ErrorEnum;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ForbiddenError implements ErrorEnum {
    NO_PERMISSION("Access Token does not have permission"),

    ;
    private final HttpStatus status = HttpStatus.FORBIDDEN;
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
