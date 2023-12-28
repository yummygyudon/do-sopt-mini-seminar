package org.sopt.global.external.oauth.client.info;

import org.sopt.entity.enums.RegisterPlatform;
import org.sopt.exception.UnauthorizedException;
import org.sopt.exception.error.UnauthorizedError;
import org.sopt.global.external.oauth.client.info.google.GoogleUserInfo;
import org.sopt.global.external.oauth.client.info.kakao.KakaoUserInfo;
import org.sopt.global.external.oauth.client.info.naver.NaverUserInfo;

import java.util.Map;

public abstract class OAuthUserInfoMapper {

    public static OAuthUserInfo of(RegisterPlatform platform, Map<String, Object> attributes) {
        switch (platform) {
            case GOOGLE -> {
                return fromGoogle(attributes);
            }
            case KAKAO -> {
                return fromKakao(attributes);
            }
            case NAVER -> {
                return fromNaver(attributes);
            }
            default -> throw new UnauthorizedException(UnauthorizedError.UNSUPPORTED_PLATFORM);
        }
    }

    private static GoogleUserInfo fromGoogle(Map<String, Object> attributes) {
        return new GoogleUserInfo(attributes);
    }

    private static KakaoUserInfo fromKakao(Map<String, Object> attributes) {
        return new KakaoUserInfo(attributes);
    }

    private static NaverUserInfo fromNaver(Map<String, Object> attributes) {
        return new NaverUserInfo(attributes);
    }
}
