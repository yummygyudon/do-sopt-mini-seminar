package org.sopt.presentation.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.sopt.application.auth.internal.InternalAuthService;
import org.sopt.common.model.ApiResponse;
import org.sopt.common.model.AuthTokenResponse;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.sopt.presentation.auth.dto.request.AuthRequest;
import org.sopt.presentation.auth.dto.request.AuthRequest.InternalAuthRequest;
import org.sopt.presentation.auth.dto.request.AuthRequest.InternalSignupRequest;
import org.sopt.success.CreatedSuccess;
import org.sopt.success.OkSuccess;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.sopt.common.util.ConstName.REFRESH_TOKEN_NAME;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final InternalAuthService internalAuthService;
    private final JwtAuthTokenService jwtAuthTokenService;

    @PostMapping("/login/internal")
    public ApiResponse loginByInternalPlatform(
            HttpServletResponse response,
            @RequestBody InternalAuthRequest authRequest
    ) throws JsonProcessingException {
        AccessTokenInfo accessUserTokenInfo = internalAuthService.login(authRequest);

        String accessToken = jwtAuthTokenService.createAccessToken(accessUserTokenInfo);
        String refreshToken = jwtAuthTokenService.createRefreshToken(accessUserTokenInfo.getUserId());
        // 이런 방식의 `ResponseCookie` 객체를 활용하여 쿠키 등록도 가능합니다!
        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .sameSite("None")
                // TODO : HTTPS 통신이 아니면 안가진다. -> Test 할 때는 우선 False로
//                .secure(true)
                .httpOnly(true)
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
        return ApiResponse.success(OkSuccess.AUTHENTICATION_SUCCESS, AuthTokenResponse.of(accessToken));
    }
    @PostMapping("/signup/internal")
    public ApiResponse<Void> signupByInternalPlatform(
            @RequestBody InternalSignupRequest signupRequest
    ) {
        boolean isSignupNew = internalAuthService.signup(signupRequest);
        if (!isSignupNew) {
            return ApiResponse.success(OkSuccess.ALREADY_REGISTERED, null);
        }
        return ApiResponse.success(CreatedSuccess.REGISTER_USER_SUCCESS, null);
    }


}
