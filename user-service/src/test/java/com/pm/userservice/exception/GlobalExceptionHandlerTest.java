package com.pm.userservice.exception;

import com.pm.userservice.dto.UserRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void handleUserNotFoundReturnsNotFoundResponse() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/123");

        ResponseEntity<ErrorResponse> response = handler.handleUserNotFound(
                new UserNotFoundException("User not found"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found");
        assertThat(response.getBody().getPath()).isEqualTo("/users/123");
    }

    @Test
    void handleEmailAlreadyExistsReturnsConflict() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        ResponseEntity<ErrorResponse> response = handler.handleEmailAlreadyExists(
                new EmailAlreadyExistsException("Duplicate"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Duplicate");
    }

    @Test
    void handleBadRequestReturnsBadRequest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(
                new BadRequestException("Bad request"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Bad request");
    }

    @Test
    void handleValidationErrorsReturnsAggregatedMessages() throws Exception {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new UserRequestDTO(), "userRequestDTO");
        bindingResult.addError(new FieldError("userRequestDTO", "email", "must be a well-formed email address"));
        bindingResult.addError(new FieldError("userRequestDTO", "name", "must not be blank"));

        Method method = Dummy.class.getDeclaredMethod("handle", UserRequestDTO.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex, request);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getMessage()).contains("email");
        assertThat(response.getBody().getMessage()).contains("name");
    }

    @Test
    void handleGenericExceptionReturnsInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(
                new RuntimeException("boom"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Internal Server Error");
    }

    private static class Dummy {
        @SuppressWarnings("unused")
        public void handle(UserRequestDTO dto) {
        }
    }
}
