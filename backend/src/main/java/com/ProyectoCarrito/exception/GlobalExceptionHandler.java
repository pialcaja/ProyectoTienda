package com.ProyectoCarrito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// MANEJADOR GLOBAL DE EXCEPCIONES
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(
			ResourceNotFoundException ex,
			WebRequest request) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				"Not Found",
				ex.getMessage(),
				request.getDescription(false).replace("uri=", "")
		);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(
			BadRequestException ex,
			WebRequest request) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				ex.getMessage(),
				request.getDescription(false).replace("uri=", "")
		);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(
			ConflictException ex,
			WebRequest request) {
		
		ErrorResponse error = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				"Conflict",
				ex.getMessage(),
				request.getDescription(false).replace("uri=", "")
		);
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
}
