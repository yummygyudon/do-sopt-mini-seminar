package org.sopt.domain;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.error.NotFoundError;
import org.sopt.global.utils.CookieUtils;
import org.sopt.interfaces.AuthorizationTokenRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;

    /**
     * Session 을 사용하지 않기 때문에 (Session : Stateless)
     * 이후 SuccessHandler 에서 Redirect 정보를 알기 위해서는
     * OAUTH2_AUTHORIZATION_REQUEST 정보를 Cookie 를 통해 정보를 가지고 재활용 할 수 있어야 한다.
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("=== CookieAuthorizationRequestRepository ===");
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElseThrow(() -> new NotFoundException(NotFoundError.OAUTH_AUTHORIZATION_REQUEST_INFO_COOKIE_NOT_FOUNT));
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("=== Save AuthorizationRequest ===");
        if (Objects.isNull(authorizationRequest)) {
            removeAuthorizationRequest(request, response);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        /**
         * 지금은 로컬에서 Redirect 하며 확인하기 때문에 동일한 Host에서 진행되기에 URI만 맞춰주지만
         * 앱잼 등에서는 클라이언트로 Redirect를 할 수 있게끔 맞춰줘야 합니다.
          */
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        log.info("Request URL : {}", request.getRequestURL());
        log.info("Request URI : {}", request.getRequestURI());
        log.info("Redirect URI : {}", redirectUriAfterLogin);
        if (redirectUriAfterLogin != null && !redirectUriAfterLogin.isBlank()) {
            // 유효한 인가 요청의 경우, 해당 정보를 Cookie 로 저장
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * OAuth 인증 과정에서 모두 째활용이 끝난 쿠키는 탈취/리소스 낭비를 방지하기 위해 삭제해 줍니다.
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
