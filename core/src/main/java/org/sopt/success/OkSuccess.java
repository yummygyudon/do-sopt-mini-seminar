package org.sopt.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.SuccessEnum;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OkSuccess implements SuccessEnum {
    // auth
    AUTHENTICATION_SUCCESS("Authentication is successful."),

    // user
    VIEW_USER_INFO_SUCCESS(""),
    VIEW_AUTHORITY_SUCCESS(""),
    CHANGE_AUTHORITY_SUCCESS(""),
    DELETE_USER_SUCCESS(""),

    ALREADY_REGISTERED("Account Info is already registered."),
    ;
    private final HttpStatus status = HttpStatus.OK;
    private final String successMessage;

    @Override
    public int getStatusCode() {
        return status.value();
    }
}
