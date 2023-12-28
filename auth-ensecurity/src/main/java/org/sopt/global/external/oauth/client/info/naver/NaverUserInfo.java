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
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getPrimaryValue() {
        return (String) attributes.get("id");
    }

    @Override
    public RegisterPlatform getProviderPlatformName() {
        return RegisterPlatform.NAVER;
    }
}
