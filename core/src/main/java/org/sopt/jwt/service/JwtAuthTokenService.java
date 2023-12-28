package org.sopt.jwt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.entity.AuthorizationToken;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.error.NotFoundError;
import org.sopt.interfaces.AuthorizationTokenRepository;
import org.sopt.jwt.provider.JwtTokenProvider;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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

    public Boolean validateAccessToken(final String atk) {
        try {
            Claims tokenClaims = jwtTokenProvider.getTokenClaims(atk);
            return !tokenClaims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    @Transactional(readOnly = true)
    public AccessTokenInfo makeAccessTokenToInfo(final String atk) throws JsonProcessingException {
        Claims tokenClaims = jwtTokenProvider.getTokenClaims(atk);
        return (AccessTokenInfo)jwtTokenProvider.getInfoFromClaim(tokenClaims, AUTH_USER, AccessTokenInfo.class);
    }

    @Transactional
    public String createAccessToken(final AccessTokenInfo authUser) throws JsonProcessingException {
        Claims generatedClaim = jwtTokenProvider.makeInfoToClaim(AUTH_USER, authUser);
        String accessToken = jwtTokenProvider.generateToken(accessTokenSubject, accessExpiration, generatedClaim);

        Optional<AuthorizationToken> userToken = tokenRepository.findByUserId(authUser.getUserId());
        if (userToken.isPresent()) {
            userToken.get().updateAccessToken(accessToken);
            return accessToken;
        }
        AuthorizationToken token = AuthorizationToken.builder()
                .userId(authUser.getUserId())
                .accessToken(accessToken)
                .build();
        tokenRepository.save(token);
        return accessToken;
    }


    @Transactional
    public String createRefreshToken(final Long userId) {
        String refreshToken = jwtTokenProvider.generateToken(refreshTokenSubject, refreshExpiration, null);

        Optional<AuthorizationToken> userToken = tokenRepository.findByUserId(userId);
        if (userToken.isEmpty()) {
            throw new NotFoundException(NotFoundError.ACCESS_TOKEN_NOT_FOUND);
        }
        userToken.get().updateRefreshToken(refreshToken);

        return refreshToken;
    }



}
