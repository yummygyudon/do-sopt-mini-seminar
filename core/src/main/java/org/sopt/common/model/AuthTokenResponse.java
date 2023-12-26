package org.sopt.common.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTokenResponse {
    private final String grantType = "BEARER";
    private final String refreshToken = "httpOnly";
    private String accessToken;

    public static AuthTokenResponse of(String accessToken) {
        return new AuthTokenResponse(accessToken);
    }
}
