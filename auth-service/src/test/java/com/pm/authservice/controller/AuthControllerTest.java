package com.pm.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.authservice.config.SecurityConfig;
import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void loginReturnsTokenWhenAuthenticated() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("secret123");

        when(authService.authenticate(any(LoginRequestDTO.class))).thenReturn(Optional.of("token-123"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-123"));
    }

    @Test
    void loginReturnsUnauthorizedWhenAuthenticationFails() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("wrongpass");

        when(authService.authenticate(any(LoginRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validateReturnsOkWhenTokenValid() throws Exception {
        when(authService.validateToken("valid-token")).thenReturn(true);

        mockMvc.perform(get("/validate")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());
    }

    @Test
    void validateReturnsUnauthorizedWhenTokenInvalid() throws Exception {
        when(authService.validateToken("bad-token")).thenReturn(false);

        mockMvc.perform(get("/validate")
                        .header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validateReturnsUnauthorizedForInvalidHeader() throws Exception {
        mockMvc.perform(get("/validate")
                        .header("Authorization", "Token abc"))
                .andExpect(status().isUnauthorized());
    }
}
