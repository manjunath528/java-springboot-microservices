package com.pm.userservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log =
          LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // ================= USER NOT FOUND =================
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(
          UserNotFoundException ex,
          HttpServletRequest request) {

    log.warn("UserNotFoundException -> {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
  }

  // ================= EMAIL ALREADY EXISTS =================
  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
          EmailAlreadyExistsException ex,
          HttpServletRequest request) {

    log.warn("EmailAlreadyExistsException -> {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(errorResponse);
  }

  // ================= BAD REQUEST =================
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(
          BadRequestException ex,
          HttpServletRequest request) {

    log.warn("BadRequestException -> {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
  }

  // ================= VALIDATION ERRORS =================
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
          MethodArgumentNotValidException ex,
          HttpServletRequest request) {

    String validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error ->
                    error.getField() + " -> " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    log.warn("Validation failed -> {}", validationErrors);

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            validationErrors,
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
  }

  // ================= FALLBACK (500) =================
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
          Exception ex,
          HttpServletRequest request) {

    log.error("Unhandled exception occurred", ex);

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Something went wrong. Please contact support.",
            request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
  }
}
