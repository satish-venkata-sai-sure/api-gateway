package com.altimetrik.apigateway.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.altimetrik.apigateway.exception.AuthorizationHeaderMissingException;
import com.altimetrik.apigateway.exception.InvalidTokenException;

@RestControllerAdvice
public class ApplicationExceptionHandler {
	
	 @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	    @ExceptionHandler(AuthorizationHeaderMissingException.class)
	    public Map<String, String> authorizationHeaderMissingException(AuthorizationHeaderMissingException ex) {
		 Map<String, String> errorMap = new HashMap<>();
	        errorMap.put("errorMessage", ex.getMessage());
	        return errorMap;
	    }

	 @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	    @ExceptionHandler(InvalidTokenException.class)
	    public Map<String, String> invalidTokenExceptionException(InvalidTokenException ex) {
	        Map<String, String> errorMap = new HashMap<>();
	        errorMap.put("errorMessage", ex.getMessage());
	        return errorMap;
	    }

}
