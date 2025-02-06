package com.foodtaste.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorHandler> handleGlobalException(Exception ex, WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("Unexpected error occurred at {}: {}", requestDetails, ex.getMessage(), ex);

		MyErrorHandler error = new MyErrorHandler("Server Error",
				"An unexpected error occurred. Please try again later.", LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MenuItemException.class)
	public ResponseEntity<MyErrorHandler> handleMenuItemException(MenuItemException ex, WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("Menu item error at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Menu Item Error", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OrderException.class)
	public ResponseEntity<MyErrorHandler> handleOrderException(OrderException ex, WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("Order error at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Order Error", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(OrderItemException.class)
	public ResponseEntity<MyErrorHandler> handleOrderItemException(OrderItemException ex, WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("Order item error at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Order Item Error", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<MyErrorHandler> handleUserException(UserException ex, WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("User error at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("User Error", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<MyErrorHandler> handleNoHandlerFoundException(NoHandlerFoundException ex,
			WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("No handler found at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Endpoint Not Found", "The requested endpoint was not found.",
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MyErrorHandler> handleValidationException(MethodArgumentNotValidException ex,
			WebRequest request) {
		String requestDetails = request.getDescription(false);
		String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining(", "));
		MyErrorHandler error = new MyErrorHandler("Validation Error", errorMessage, LocalDateTime.now());
		log.warn("Validation errors at {}: {}", requestDetails);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MyErrorHandler> handleInvalidJson(HttpMessageNotReadableException ex, WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.error("Invalid JSON format at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Invalid JSON", "Malformed JSON request.", LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<MyErrorHandler> handleConstraintViolation(ConstraintViolationException ex,
			WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.warn("Constraint violation at {}: {}", requestDetails, ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Constraint Violation", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<MyErrorHandler> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		String requestDetails = request.getDescription(false);
		log.warn("Type mismatch at {}: Parameter '{}', Message: {}", requestDetails, ex.getName(), ex.getMessage());

		MyErrorHandler error = new MyErrorHandler("Type Mismatch", "Invalid value for parameter: " + ex.getName(),
				LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
