package org.sopt.application.auth.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.CustomOAuthUser;
import org.sopt.entity.Authority;
import org.sopt.entity.MiminarUser;
import org.sopt.entity.enums.RegisterPlatform;
import org.sopt.entity.enums.Role;
import org.sopt.exception.UnauthorizedException;
import org.sopt.global.external.oauth.client.info.OAuthUserInfoMapper;
import org.sopt.global.external.oauth.client.info.OAuthUserInfo;
import org.sopt.interfaces.AuthorityRepository;
import org.sopt.interfaces.MiminarUserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MiminarUserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("=== SocialAuthService ===");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo Json 값(유저 정보들)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        RegisterPlatform registerPlatform = RegisterPlatform.getPlatformByRegistrationId(registrationId);

        // 플랫폼마다 Map 구조가 다르기 때문에 Provider 등으로 분기처리해서 Client UserInfo Mapping 해서 가져와야함
        try {
            OAuthUserInfo oAuthUserInfo = OAuthUserInfoMapper.of(registerPlatform, attributes);
            Optional<MiminarUser> targetUser =
                    userRepository.findByEmailAndPasswordAndPlatform(
                            oAuthUserInfo.getEmail(), oAuthUserInfo.getPrimaryValue(), registerPlatform
                    );
            if (targetUser.isEmpty()) {
                MiminarUser miminarUser = registerNewUser(oAuthUserInfo, registerPlatform);
                Authority authority = registerNewAuthorize(miminarUser);
                return new CustomOAuthUser(
                        miminarUser,
                        List.of(authority),
                        attributes
                );
            }
            List<Authority> userAuthorities = authorityRepository.findAllByUserId(targetUser.get().getId());
            return new CustomOAuthUser(
                    targetUser.get(),
                    userAuthorities,
                    attributes
            );
        } catch (UnauthorizedException e) {
            throw new OAuth2AuthenticationException(e.getError().getErrorMessage());
        }
    }

    @Transactional
    MiminarUser registerNewUser(OAuthUserInfo oAuth2User, RegisterPlatform platform) {
        MiminarUser miminarUser = MiminarUser.builder()
                .name(oAuth2User.getName())
                .password(oAuth2User.getPrimaryValue())
                .email(oAuth2User.getEmail())
                .platform(platform)
                .build();
        return userRepository.save(miminarUser);
    }
    @Transactional
    Authority registerNewAuthorize(MiminarUser user) {
        Authority defaultAuthority = Authority.builder()
                .userId(user.getId())
                .role(Role.MEMBER)
                .build();
        return authorityRepository.save(defaultAuthority);
    }
}
