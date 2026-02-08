package com.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    @Test
    void generateTokenProducesNonEmptyToken() {
        JwtUtil jwtUtil = new JwtUtil(validSecret());

        String token = jwtUtil.generateToken("user@example.com", "USER");

        assertThat(token).isNotBlank();
    }

    @Test
    void validateTokenAcceptsValidToken() {
        JwtUtil jwtUtil = new JwtUtil(validSecret());
        String token = jwtUtil.generateToken("user@example.com", "USER");

        jwtUtil.validateToken(token);
    }

    @Test
    void validateTokenRejectsTamperedToken() {
        JwtUtil jwtUtil = new JwtUtil(validSecret());
        String token = jwtUtil.generateToken("user@example.com", "USER");

        String tampered = token.substring(0, token.length() - 1) + (token.endsWith("a") ? "b" : "a");

        assertThatThrownBy(() -> jwtUtil.validateToken(tampered))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void constructorThrowsOnInvalidBase64Secret() {
        assertThatThrownBy(() -> new JwtUtil("not-base64"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static String validSecret() {
        byte[] key = "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(key);
    }
}
