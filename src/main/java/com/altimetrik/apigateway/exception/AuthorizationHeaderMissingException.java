package com.altimetrik.apigateway.exception;

public class AuthorizationHeaderMissingException extends RuntimeException {
    public AuthorizationHeaderMissingException(String message) {
        super(message);
    }
}