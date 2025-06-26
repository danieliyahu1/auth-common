package com.connect.auth.common.service;

import java.util.UUID;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.util.Date;

import static com.connect.auth.common.constants.JwtConstants.ACCESS_TOKEN_LIFE_SPAN;
import static com.connect.auth.common.constants.JwtConstants.REFRESH_TOKEN_LIFE_SPAN;

public final class TestTokenGenerator {

    public static String generateAccessToken(UUID userId, PrivateKey privateKey) {
        return generateToken(userId, "access", ACCESS_TOKEN_LIFE_SPAN, privateKey);
    }

    public static String generateRefreshToken(UUID userId, PrivateKey privateKey) {
        return generateToken(userId, "refresh", REFRESH_TOKEN_LIFE_SPAN, privateKey);
    }

    private static String generateToken(UUID userId, String tokenType, long expirationMillis, PrivateKey privateKey) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("token_type", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(privateKey)
                .compact();
    }
}
