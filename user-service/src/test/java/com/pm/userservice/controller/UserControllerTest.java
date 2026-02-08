package com.pm.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.exception.GlobalExceptionHandler;
import com.pm.userservice.model.FitnessGoal;
import com.pm.userservice.model.Gender;
import com.pm.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getUsersReturnsOk() throws Exception {
        UserResponseDTO user = sampleResponse();
        when(userService.getUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }

    @Test
    void createUserReturnsCreated() throws Exception {
        UserRequestDTO request = sampleRequest();
        UserResponseDTO response = sampleResponse();
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    void createUserReturnsBadRequestForInvalidPayload() throws Exception {
        UserRequestDTO request = sampleRequest();
        request.setName("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void getUserDetailsReturnsOk() throws Exception {
        UserResponseDTO response = sampleResponse();
        when(userService.getUserById(UUID.fromString(response.getId()))).thenReturn(response);

        mockMvc.perform(get("/users/{id}", response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void updateUserReturnsOk() throws Exception {
        UserRequestDTO request = sampleRequest();
        UserResponseDTO response = sampleResponse();
        when(userService.updateUser(any(UUID.class), any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/users/{id}", response.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    void deleteUserReturnsNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    private static UserRequestDTO sampleRequest() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Alex");
        request.setEmail("alex@example.com");
        request.setAddress("123 Main St");
        request.setWeight(70.0);
        request.setHeight(1.72);
        request.setGender(Gender.MALE);
        request.setDateOfBirth("1990-01-01");
        request.setFitnessGoal(FitnessGoal.GENERAL_FITNESS);
        request.setDailyStepGoal(8000);
        request.setSleepGoalHours(8.0);
        request.setNotificationsEnabled(true);
        return request;
    }

    private static UserResponseDTO sampleResponse() {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(UUID.randomUUID().toString());
        response.setName("Alex");
        response.setEmail("alex@example.com");
        response.setAddress("123 Main St");
        response.setWeight(70.0);
        response.setHeight(1.72);
        response.setGender(Gender.MALE);
        response.setDateOfBirth("1990-01-01");
        response.setFitnessGoal(FitnessGoal.GENERAL_FITNESS);
        response.setDailyStepGoal(8000);
        response.setSleepGoalHours(8.0);
        response.setNotificationsEnabled(true);
        return response;
    }
}
