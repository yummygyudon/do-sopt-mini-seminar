package org.sopt.global.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.common.model.AuthTokenResponse;
import org.sopt.common.util.RequestUtils;
import org.sopt.domain.CustomOAuthUser;
import org.sopt.entity.MiminarUser;
import org.sopt.global.utils.CookieUtils;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.sopt.success.OkSuccess;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.sopt.common.util.ConstName.REFRESH_TOKEN_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthSuccessHandler2 extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtAuthTokenService jwtAuthTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("=== OAuth2AuthSuccessHandler ===");
        if (response.isCommitted()) {
            // 이미 등록된 Response. 즉, 이미 정상 종료된 인증 외 일반적인 API 요청들에 대한 Response 는 그냥 보내줍니다.
            return;
            /**
             * Commit 여부로 인증 요청 여부를 분류할 수 있는 이유
             * : 해당 Request는 일반적인 경우처럼 Controller 등으로 return 처리되는 경우가 아닌
             *   Security OAuthClient에 의해 진행되는 중의 요청이기 때문이다.
             */
        }
        //JWT 생성
        CustomOAuthUser customOAuthUser = (CustomOAuthUser)authentication.getPrincipal();
        MiminarUser userPrincipal = customOAuthUser.getCustomUser();

        AccessTokenInfo accessTokenInfo = AccessTokenInfo.of(userPrincipal.getId());
        String accessToken = jwtAuthTokenService.createAccessToken(accessTokenInfo);
        String refreshToken = jwtAuthTokenService.createRefreshToken(userPrincipal.getId());

        CookieUtils.addCookie(response, REFRESH_TOKEN_NAME, refreshToken,7 * 24 * 60 * 60); // RTK Cookie 수명 : 7일로 설정
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);


        RequestUtils.setBodyOnResponse(
                response, OkSuccess.AUTHENTICATION_SUCCESS, AuthTokenResponse.of(accessToken)
        );
    }
}
