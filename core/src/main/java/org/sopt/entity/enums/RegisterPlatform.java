package org.sopt.entity.enums;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.error.NotFoundError;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RegisterPlatform {
    INTERNAL("internal"),
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google")
    ;

    private final String registrationId;

    public static RegisterPlatform getPlatformByRegistrationId(String registrationId) {
        return Arrays.stream(values())
                .filter(platform -> platform.registrationId.equals(registrationId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(NotFoundError.REGISTRATION_PLATFORM_NOT_FOUND));
    }
}
