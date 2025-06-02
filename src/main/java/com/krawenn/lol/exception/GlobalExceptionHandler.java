package com.krawenn.lol.exception;

import com.krawenn.lol.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RiotGamesBusinessException.class)
    public ResponseEntity<Object> handleBusinessException(RiotGamesBusinessException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RiotGamesSystemException.class)
    public ResponseEntity<Object> handleSystemException(RiotGamesSystemException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse("An error occurred while contacting Riot Games: " + ex.getMessage()));
    }
} 