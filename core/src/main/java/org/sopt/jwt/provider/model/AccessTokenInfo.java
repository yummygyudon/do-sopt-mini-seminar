package org.sopt.jwt.provider.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@Value
@ToString
public class AccessTokenInfo implements TokenInfo{

    private Long userId;

    public static AccessTokenInfo of(Long userId) {
        return new AccessTokenInfo(userId);
    }
}
