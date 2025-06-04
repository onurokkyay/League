package com.krawenn.lol.exception;

import com.krawenn.lol.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RiotGamesBusinessException.class)
    public ResponseEntity<Object> handleBusinessException(RiotGamesBusinessException ex) {
        log.warn("RiotGamesBusinessException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RiotGamesSystemException.class)
    public ResponseEntity<Object> handleSystemException(RiotGamesSystemException ex) {
        log.error("RiotGamesSystemException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse("An error occurred while contacting Riot Games: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(new ErrorResponse("Internal server error: " + ex.getMessage()));
    }
} 