package org.sopt.jwt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.common.model.AuthTokenResponse;
import org.sopt.entity.AuthorizationToken;
import org.sopt.interfaces.AuthorizationTokenRepository;
import org.sopt.interfaces.MiminarUserRepository;
import org.sopt.jwt.provider.JwtTokenProvider;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.provider.model.TokenInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.sopt.common.util.ConstName.AUTH_USER;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtAuthTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationTokenRepository tokenRepository;
    @Value("${jwt.access.subject}")
    private String accessTokenSubject;
    @Value("${jwt.refresh.subject}")
    private String refreshTokenSubject;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Transactional(readOnly = true)
    public AccessTokenInfo makeAccessTokenToInfo(final String atk) throws JsonProcessingException {
        Claims tokenClaims = jwtTokenProvider.getTokenClaims(atk);
        return (AccessTokenInfo)jwtTokenProvider.getInfoFromClaim(tokenClaims, AUTH_USER, AccessTokenInfo.class);
    }

    @Transactional
    public String createAccessToken(final AccessTokenInfo authUser) throws JsonProcessingException {
        Claims generatedClaim = jwtTokenProvider.makeInfoToClaim(AUTH_USER, authUser);
        String accessToken = jwtTokenProvider.generateToken(accessTokenSubject, accessExpiration, generatedClaim);

        AuthorizationToken userToken = tokenRepository.findByUserId(authUser.getUserId());
        userToken.updateAccessToken(accessToken);

        return accessToken;
    }


    @Transactional
    public String createRefreshToken(final Long userId) {
        String refreshToken = jwtTokenProvider.generateToken(refreshTokenSubject, refreshExpiration, null);

        AuthorizationToken userToken = tokenRepository.findByUserId(userId);
        userToken.updateRefreshToken(refreshToken);

        return refreshToken;
    }

}
