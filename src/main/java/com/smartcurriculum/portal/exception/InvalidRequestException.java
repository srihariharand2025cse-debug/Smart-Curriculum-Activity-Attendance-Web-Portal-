package com.smartcurriculum.portal.exception;

/**
 * Custom RuntimeException thrown when a client request contains invalid or incomplete data
 * that cannot be processed by the service layer.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
