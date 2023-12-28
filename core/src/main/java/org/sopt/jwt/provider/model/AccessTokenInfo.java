package org.sopt.jwt.provider.model;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@Value
@ToString
public class AccessTokenInfo implements TokenInfo{

    private Long userId;

    public static AccessTokenInfo of(Long userId) {
        return new AccessTokenInfo(userId);
    }
}
