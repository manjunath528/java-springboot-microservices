package com.pm.activityservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.activityservice.dto.ActivityRequest;
import com.pm.activityservice.dto.ActivityResponse;
import com.pm.activityservice.exception.GlobalExceptionHandler;
import com.pm.activityservice.model.ActivityStatus;
import com.pm.activityservice.model.WorkoutType;
import com.pm.activityservice.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
@Import(GlobalExceptionHandler.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActivityService activityService;

    @Test
    void createActivityReturnsCreated() throws Exception {
        ActivityRequest request = sampleRequest();
        ActivityResponse response = sampleResponse();
        when(activityService.createActivity(any(ActivityRequest.class))).thenReturn(response);

        mockMvc.perform(post("/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId().toString()));
    }

    @Test
    void updateStatusReturnsOk() throws Exception {
        ActivityResponse response = sampleResponse();
        when(activityService.updateStatus(eq(response.getId()), eq("COMPLETED"))).thenReturn(response);

        mockMvc.perform(patch("/activities/{activityId}/status", response.getId())
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void getUserActivitiesReturnsOk() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityResponse response = sampleResponse();
        when(activityService.getUserActivities(userId)).thenReturn(List.of(response));

        mockMvc.perform(get("/activities/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId().toString()));
    }

    @Test
    void getUserActivitiesByDateReturnsOk() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityResponse response = sampleResponse();
        when(activityService.getUserActivitiesByDate(userId, LocalDate.parse("2024-01-10")))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/activities/user/{userId}/date", userId)
                        .param("date", "2024-01-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId().toString()));
    }

    private static ActivityRequest sampleRequest() {
        ActivityRequest request = new ActivityRequest();
        request.setUserId(UUID.randomUUID());
        request.setWorkoutType(WorkoutType.RUNNING);
        request.setDurationMinutes(30);
        request.setCaloriesBurned(250);
        request.setActivityDate(LocalDate.parse("2024-01-10"));
        request.setStatus("COMPLETED");
        return request;
    }

    private static ActivityResponse sampleResponse() {
        ActivityResponse response = new ActivityResponse();
        response.setId(UUID.randomUUID());
        response.setUserId(UUID.randomUUID());
        response.setWorkoutType(WorkoutType.RUNNING);
        response.setDurationMinutes(30);
        response.setCaloriesBurned(250);
        response.setActivityDate(LocalDate.parse("2024-01-10"));
        response.setStatus(ActivityStatus.COMPLETED);
        return response;
    }
}
