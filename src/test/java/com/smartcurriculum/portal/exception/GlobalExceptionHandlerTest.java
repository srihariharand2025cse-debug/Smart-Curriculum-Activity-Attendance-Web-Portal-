package com.smartcurriculum.portal.exception;

import com.smartcurriculum.portal.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests verifying error handling across all custom and Spring framework exceptions
 * mapped in GlobalExceptionHandler — Day 18 error handling & bug fixing.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with 404 NOT_FOUND")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Student", "id", 101L);
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleResourceNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Student not found with id: '101'", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle DuplicateResourceException with 409 CONFLICT")
    void shouldHandleDuplicateResourceException() {
        DuplicateResourceException ex = new DuplicateResourceException("Student", "rollNumber", "21CSE001");
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleDuplicateResourceException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Student already exists with rollNumber: '21CSE001'", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle InvalidRequestException with 400 BAD_REQUEST")
    void shouldHandleInvalidRequestException() {
        InvalidRequestException ex = new InvalidRequestException("Invalid roll number format");
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleInvalidRequestException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid roll number format", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with 400 BAD_REQUEST")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Department cannot be null");
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Department cannot be null", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with 400 BAD_REQUEST and field error map")
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fe1 = new FieldError("student", "rollNumber", "Roll number is required");
        FieldError fe2 = new FieldError("student", "email", "Invalid email format");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fe1, fe2));

        ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Validation failed for 2 field(s)"));
        assertEquals("Roll number is required", response.getBody().getData().get("rollNumber"));
        assertEquals("Invalid email format", response.getBody().getData().get("email"));
    }

    @Test
    @DisplayName("Should handle MissingServletRequestParameterException with 400 BAD_REQUEST")
    void shouldHandleMissingServletRequestParameterException() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("status", "String");
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleMissingParam(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Required parameter 'status'"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException with 400 BAD_REQUEST")
    void shouldHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");
        when(ex.getValue()).thenReturn("abc");
        when(ex.getRequiredType()).thenReturn((Class) Long.class);

        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleTypeMismatch(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Parameter 'id' should be of type 'Long'"));
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with 400 BAD_REQUEST")
    void shouldHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleMalformedJson(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Malformed or unreadable JSON"));
    }

    @Test
    @DisplayName("Should handle HttpRequestMethodNotSupportedException with 405 METHOD_NOT_ALLOWED")
    void shouldHandleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException ex =
                new HttpRequestMethodNotSupportedException("POST", List.of("GET", "PUT"));
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleMethodNotAllowed(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("HTTP method 'POST' is not supported"));
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with 409 CONFLICT")
    void shouldHandleDataIntegrityViolationException() {
        Throwable cause = new RuntimeException("Duplicate entry '21CSE001' for key 'uk_students_roll'");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Constraint violation", cause);

        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleDataIntegrityViolation(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("A database constraint was violated"));
    }

    @Test
    @DisplayName("Should handle NoResourceFoundException with 404 NOT_FOUND")
    void shouldHandleNoResourceFoundException() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        when(ex.getResourcePath()).thenReturn("/unknown/asset.png");

        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleNoResourceFound(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("/unknown/asset.png"));
    }

    @Test
    @DisplayName("Should handle unhandled generic Exception with 500 INTERNAL_SERVER_ERROR")
    void shouldHandleGenericException() {
        Exception ex = new NullPointerException("Something was null unexpectedly");
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleGlobalException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("An unexpected error occurred"));
    }
}
