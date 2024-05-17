package com.example.mdmggreal.global.security;


import com.example.mdmggreal.global.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.example.mdmggreal.global.exception.ErrorCode.*;


@Slf4j
@Component
public class JwtUtil {
    private static Key key = null;
    private final long accessTokenExpTime;

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey,
                   @Value("${jwt.access.expiration}") long accessTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(CustomUserInfoDto user) {
        return createToken(user, accessTokenExpTime);
    }

    public static String createToken(CustomUserInfoDto user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("mobile", user.getMobile());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidTime = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public static String getMobile(String token) {
        if (token.split(" ").length > 1) {
            token = token.split(" ")[1].trim();
        }
        return parseClaims(token).get("mobile",String.class);
    }

    public static boolean validateToken(String token) {
        try {
            token = token.split(" ")[1].trim();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new CustomException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(JWT_CLAIMS_EMPTY);
        }
    }


    public static Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        }

    }


}