package org.sopt.global.filter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sopt.application.auth.internal.InternalAuthService;
import org.sopt.common.util.RequestUtils;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.UnauthorizedException;
import org.sopt.exception.error.UnauthorizedError;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

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
