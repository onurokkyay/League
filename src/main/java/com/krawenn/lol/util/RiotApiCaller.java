package com.krawenn.lol.util;

import com.krawenn.lol.exception.RiotGamesBusinessException;
import com.krawenn.lol.exception.RiotGamesSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.function.Supplier;

public class RiotApiCaller {
    public static <T> T execute(Supplier<T> action) {
        try {
            return action.get();
        } catch (HttpClientErrorException e) {
            throw new RiotGamesBusinessException(e.getMessage(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new RiotGamesSystemException(
                e.getMessage(),
                e instanceof HttpServerErrorException
                    ? HttpStatus.valueOf(((HttpServerErrorException) e).getStatusCode().value())
                    : HttpStatus.GATEWAY_TIMEOUT
            );
        }
    }
} 