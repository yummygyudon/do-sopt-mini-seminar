package org.sopt.global.filter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.sopt.application.auth.internal.InternalAuthService;
import org.sopt.common.util.RequestUtils;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.UnauthorizedException;
import org.sopt.exception.error.UnauthorizedError;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
/**
 * 인증이 되면 SecurityContext에 보관하여 보안을 통과
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthTokenService jwtAuthTokenService;
    private final InternalAuthService internalAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (RequestUtils.isContainsAccessToken(request)) {
            String authorizationAccessToken = RequestUtils.getAuthorizationAccessToken((HttpServletRequest) request);;
            if (jwtAuthTokenService.validateAccessToken(authorizationAccessToken)) {
                try {
                    AccessTokenInfo accessTokenInfo = jwtAuthTokenService.makeAccessTokenToInfo(authorizationAccessToken);

                    UserDetails userDetails = internalAuthService.loadUserByUsername(accessTokenInfo.getUserId().toString());

                    Authentication authentication
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    /**
                     * 이런 방식도 있음
                     * (단, OncePerRequestFilter 같은 ServletFilter 가 아닌 UsernamePasswordAuthenticationFilter 를 Implements 해서 구현해야함)
                     *
                     * 1. AuthenticationManager 의 authenticate() 함수가 호출
                     *    -> 인증 프로바이더가 유저 디테일 서비스의 loadUserByUsername(`UsernamePasswordAuthenticationToken` 토큰의 첫번째 파라미터) 를 호출
                     * 2. UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과 UserDetails(DB값)의 getPassword()함수로 비교
                     *    -> 동일하면 Authentication 객체를 만들어서 필터체인으로 리턴
                     * 3. Security Session 영역에 Authentication 객체가 저장
                     *
                     *    Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
                     *    Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
                     *    => 인증 프로바이더에게 뭐 알려줄 필요가 없음.
                     */
                    // Authentication authentication = authenticationManager.authenticate(authenticationToken);
                    // return authentication

                } catch (NotFoundException e) {
                    throw new UnauthorizedException(UnauthorizedError.INVALID_ACCESS_TOKEN);
                }
            } else {
                throw new UnauthorizedException(UnauthorizedError.INVALID_ACCESS_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }
}
