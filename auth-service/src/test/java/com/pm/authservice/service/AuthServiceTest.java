package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticateReturnsTokenWhenCredentialsMatch() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("secret123");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("hashed");
        user.setRole("USER");

        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("user@example.com", "USER")).thenReturn("token-123");

        Optional<String> token = authService.authenticate(request);

        assertThat(token).contains("token-123");
    }

    @Test
    void authenticateReturnsEmptyWhenUserMissing() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("missing@example.com");
        request.setPassword("secret123");

        when(userService.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        Optional<String> token = authService.authenticate(request);

        assertThat(token).isEmpty();
    }

    @Test
    void authenticateReturnsEmptyWhenPasswordDoesNotMatch() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("wrong");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("hashed");
        user.setRole("USER");

        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        Optional<String> token = authService.authenticate(request);

        assertThat(token).isEmpty();
    }

    @Test
    void validateTokenReturnsTrueWhenJwtValid() {
        org.mockito.Mockito.doNothing().when(jwtUtil).validateToken("token");

        boolean valid = authService.validateToken("token");

        assertThat(valid).isTrue();
    }

    @Test
    void validateTokenReturnsFalseWhenJwtInvalid() {
        org.mockito.Mockito.doThrow(new JwtException("Invalid"))
                .when(jwtUtil)
                .validateToken("bad-token");

        boolean valid = authService.validateToken("bad-token");

        assertThat(valid).isFalse();
    }
}
