package org.sopt.global.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.CookieAuthorizationRequestRepository;
import org.sopt.domain.CustomOAuthUser;
import org.sopt.entity.MiminarUser;
import org.sopt.exception.BadRequestException;
import org.sopt.exception.error.BadRequestError;
import org.sopt.global.utils.CookieUtils;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static org.sopt.common.util.ConstName.REFRESH_TOKEN_NAME;
import static org.sopt.domain.CookieAuthorizationRequestRepository.*;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * oauth 인증이 성공 후, CustomOAuth2UserService(`SocialAuthService.class`)를 거쳐
 * 마지막으로 실행되는 부분
 *
 * 해당 핸들러에서 security 사용자 인증 정보(`Authentication.class`)를 통해
 * jwt access token을 생성 => 최초 oauth 인증 요청 시 받았던 redirect_uri를 검증한 후, 해당 uri로 access token Response
 */
public class OAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtAuthTokenService jwtAuthTokenService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

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
        log.info("URL : {}",request.getRequestURL().toString());
        log.info("code : {}",request.getParameter("code"));
        String targetUrl = decideTargetUrl(request, response, authentication);
        // 인증 요청과 인증 정보에 대한 활용이 끝난 이후, 깨끗이 비워줍니다.
        clearAuthenticationAttributes(request,response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    private String decideTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws JsonProcessingException {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(BadRequestError.INVALID_REDIRECT_URI);
        }

        // Config에서 설정했던 base target url
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        log.info("Target URL : {}", targetUrl);

        //JWT 생성
        CustomOAuthUser customOAuthUser = (CustomOAuthUser)authentication.getPrincipal();
        MiminarUser userPrincipal = customOAuthUser.getCustomUser();

        AccessTokenInfo accessTokenInfo = AccessTokenInfo.of(userPrincipal.getId());
        String accessToken = jwtAuthTokenService.createAccessToken(accessTokenInfo);
        String refreshToken = jwtAuthTokenService.createRefreshToken(userPrincipal.getId());

        CookieUtils.addCookie(response, REFRESH_TOKEN_NAME, refreshToken,7 * 24 * 60 * 60); // RTK Cookie 수명 : 7일로 설정
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);


        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .queryParam("error", "")
                .build().encode().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);
        log.info("Client Redirect : {}", clientRedirectUri.getPath());
        log.info("Client Host : {}", clientRedirectUri.getHost());
        log.info("Client Port : {}", clientRedirectUri.getPort());
        log.info("Authorized Redirect : {}", authorizedUri.getPath());
        log.info("Authorized Host : {}", authorizedUri.getHost());
        log.info("Authorized Port : {}", authorizedUri.getPort());

        if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
