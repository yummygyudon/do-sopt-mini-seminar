package org.sopt.jwt.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.sopt.exception.error.InternalServerError;
import org.sopt.exception.InternalServerException;
import org.sopt.jwt.provider.model.TokenInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final ObjectMapper objectMapper;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, ObjectMapper objectMapper) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.objectMapper = objectMapper;
    }

    public String generateToken(String subject, Long expiration, final Claims claims) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * [[ 주요 Exception ]]
     * - 큰SignatureException
     *   : signature 검증이 실패했을 때 발생합니다
     * - MalformedJwtException
     *   : JWS 형식에 맞지 않을 때 발생합니다.
     * - ExpiredJwtException
     *   : 토큰이 만료되었을 때 발생합니다.
     */
    public Claims getTokenClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims makeInfoToClaim(String infoName, final TokenInfo info) throws JsonProcessingException {
        String claimValue = objectMapper.writeValueAsString(info);
        Claims claims = Jwts.claims();
        claims.put(infoName, claimValue);
        return claims;
    }

    public <T extends TokenInfo> TokenInfo getInfoFromClaim(Claims claims, String infoName, Class<T> infoType) throws JsonProcessingException {
        String infoJson = claims.get(infoName, String.class);
        if (!TokenInfo.class.isAssignableFrom(infoType)) {
            throw new InternalServerException(InternalServerError.PARSE_ERROR);
        }
        return objectMapper.readValue(infoJson, infoType);
    }
}
