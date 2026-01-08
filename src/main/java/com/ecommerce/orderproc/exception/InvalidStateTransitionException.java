package com.ecommerce.orderproc.exception;

// Custom exception for business logic errors regarding order states
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}