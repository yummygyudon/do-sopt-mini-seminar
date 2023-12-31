package org.sopt.global.external.oauth.client.info.kakao;

import lombok.*;
import org.sopt.entity.enums.RegisterPlatform;
import org.sopt.global.external.oauth.client.info.OAuthUserInfo;

import java.util.Map;

@ToString
@Getter
public class KakaoUserInfo extends OAuthUserInfo {


    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getPrimaryValue() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public RegisterPlatform getProviderPlatformName() {
        return RegisterPlatform.KAKAO;
    }
}
