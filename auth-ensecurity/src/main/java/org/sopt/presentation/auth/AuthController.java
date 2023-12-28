package org.sopt.presentation.auth;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.sopt.application.auth.internal.InternalAuthService;
import org.sopt.common.model.ApiResponse;
import org.sopt.presentation.auth.dto.request.AuthRequest;
import org.sopt.presentation.auth.dto.request.AuthRequest.InternalAuthRequest;
import org.sopt.presentation.auth.dto.request.AuthRequest.InternalSignupRequest;
import org.sopt.success.CreatedSuccess;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

//    private final AuthenticationManager authenticationManager;
    private final InternalAuthService internalAuthService;


    @GetMapping("/token")
    public String token(@RequestParam String token, @RequestParam String error) {
        if (!error.isBlank()) {
            return error;
        }
        return token;
    }

    @PostMapping("/login/social")
    public ApiResponse loginBySocialPlatform() {

        return ApiResponse.success(CreatedSuccess.REISSUE_TOKEN_SUCCESS);
    }

    @PostMapping("/login/internal")
    public ApiResponse loginByInternalPlatform(
            @RequestBody InternalAuthRequest authRequest
    ) {
//        UsernamePasswordAuthenticationToken authenticationRequest =
//                UsernamePasswordAuthenticationToken.unauthenticated(authRequest.username(), authRequest.password());
//        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        return ApiResponse.success(CreatedSuccess.REISSUE_TOKEN_SUCCESS);
    }
    @PostMapping("/signup/internal")
    public ApiResponse signupByInternalPlatform(
            @RequestBody InternalSignupRequest signupRequest
    ) {
        internalAuthService.signup(signupRequest);
        return ApiResponse.success(CreatedSuccess.REISSUE_TOKEN_SUCCESS);
    }

    @PostMapping("/logout")
    public ApiResponse logout() {

        return ApiResponse.success(CreatedSuccess.REISSUE_TOKEN_SUCCESS);
    }

}
