package org.sopt.global.external.oauth.client.info.naver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.sopt.entity.enums.RegisterPlatform;
import org.sopt.global.external.oauth.client.info.OAuthUserInfo;

import java.util.Map;

@ToString
@Getter
public class NaverUserInfo extends OAuthUserInfo {


    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
        return (String) naverAccount.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
        return (String) naverAccount.get("email");
    }

    @Override
    public String getPrimaryValue() {
        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
        return (String) naverAccount.get("id");
    }

    @Override
    public RegisterPlatform getProviderPlatformName() {
        return RegisterPlatform.NAVER;
    }
}
