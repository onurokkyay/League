package com.krawenn.lol.exception;

import com.krawenn.lol.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RiotGamesBusinessException.class)
    public ResponseEntity<Object> handleBusinessException(RiotGamesBusinessException ex) {
        logger.warn("RiotGamesBusinessException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RiotGamesSystemException.class)
    public ResponseEntity<Object> handleSystemException(RiotGamesSystemException ex) {
        logger.error("RiotGamesSystemException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse("An error occurred while contacting Riot Games: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        logger.error("Internal server error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(new ErrorResponse("Internal server error: " + ex.getMessage()));
    }
} 