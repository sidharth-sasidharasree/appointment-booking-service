package com.booking.portal.config;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.booking.portal.dto.ErrorResponse;
import com.booking.portal.exception.ApiException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(basePackages = "com.booking.portal.controller")
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return buildErrorResponse("Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		String errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(", "));

		return buildErrorResponse("Validation failed: " + errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
		String path = request.getRequestURI();

		// Exclude swagger-ui.html and api-docs endpoints from this handler
		if (path.equals("/swagger-ui.html") || path.startsWith("/api-docs")) {
			// Return null to let Spring's default handling process this
			return null;
		}

		// Build your custom error response for all other exceptions
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
		ErrorResponse error = new ErrorResponse(ex.getStatus().value(), ex.getStatus().getReasonPhrase(),ex.getMessage());
		return new ResponseEntity<>(error, ex.getStatus());
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
		ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(), message);
		return new ResponseEntity<>(error, status);
	}
}
