package ru.practicum.exploreWithMe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestControllerAdvice("ru.practicum.exploreWithMe.controller")
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.CREATED)
    public ErrorBody handleOk(HttpClientErrorException e) {
        log.info("--==>>REST Throwable: /{}/", e.getMessage());
        return new ErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, List.of("unchecked error"));
    }
}
