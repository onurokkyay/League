package com.krawenn.lol.exception;

import org.springframework.http.HttpStatus;

public class RiotGamesSystemException extends RuntimeException {
    private final HttpStatus status;

    public RiotGamesSystemException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
} 