package com.jai.HireBridge.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler 
{

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex){
		return buildResponse(ex.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Object> handleDuplicate(DuplicateResourceException ex){
		return buildResponse(ex.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex){
		return buildResponse(ex.getMessage(),HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<Object> handleBusinessRule(BusinessRuleException ex){
		return buildResponse(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<Object> buildResponse(String message,HttpStatus status){
		Map<String,Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return new ResponseEntity<>(body,status);
	}
	
}
 