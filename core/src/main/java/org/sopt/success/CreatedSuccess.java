package org.sopt.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.SuccessEnum;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CreatedSuccess implements SuccessEnum {

    // auth
    REISSUE_TOKEN_SUCCESS("Authorization Token Reissue is successful."),

    // user
    REGISTER_USER_SUCCESS("User Registration is successful."),
    ;
    private final HttpStatus status = HttpStatus.CREATED;
    private final String successMessage;

    @Override
    public int getStatusCode() {
        return status.value();
    }
}
