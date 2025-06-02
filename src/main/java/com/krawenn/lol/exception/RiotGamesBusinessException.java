package com.krawenn.lol.exception;

import org.springframework.http.HttpStatus;

public class RiotGamesBusinessException extends RuntimeException {
    private final HttpStatus status;

    public RiotGamesBusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
} 