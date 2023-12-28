package org.sopt.exception.error;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.ErrorEnum;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BadRequestError implements ErrorEnum {

    // auth
    INVALID_REDIRECT_URI("Redirect URIs are not matched."),

    // auth (token)
    NO_ACCESS_TOKEN("Access Token does not exist."),
    CONTENT_PARSE_FAILED("Token Json Parse Failed."),
    INVALID_ACCESS_TOKEN("Access Token is invalid."),
    NO_CONTENTS_IN_CLAIMS("Token Claims string is empty."),
    WRONG_CONTENTS_FOR_CLAIMS("Token Claims string is unavailable."),
    NO_REQUIRED_AUTH_INFO_IN_CLAIMS("Token Claims doesn't have Required information."),

    ;

    private final HttpStatus status = HttpStatus.BAD_REQUEST;
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
