package org.sopt.application.user.info;

import java.util.List;

public abstract class UserInfo {

    public record UserProfile(
        String name,
        String email,
        List<String> roles,
        String registerPlatform
    ) {
    }
}

