package com.smartcurriculum.portal.exception;

/**
 * Custom RuntimeException thrown when an entity creation or update conflicts
 * with an existing record due to unique field constraints (e.g., rollNumber, email).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
