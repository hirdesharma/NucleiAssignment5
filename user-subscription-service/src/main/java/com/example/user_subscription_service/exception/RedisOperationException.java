package com.example.user_subscription_service.exception;

public class RedisOperationException extends RuntimeException {
    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
