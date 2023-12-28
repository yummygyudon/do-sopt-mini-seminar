package org.sopt.global.external.oauth.client.info;

import lombok.AllArgsConstructor;
import org.sopt.entity.enums.RegisterPlatform;

import java.util.Map;

@AllArgsConstructor
public abstract class OAuthUserInfo {

    protected Map<String,Object> attributes;

    public abstract String getName();
    public abstract String getEmail();
    public abstract String getPrimaryValue();
    public abstract RegisterPlatform getProviderPlatformName();


}
