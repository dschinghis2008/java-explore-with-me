package ru.practicum.exploreWithMe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice("ru.practicum.exploreWithMe.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBody handleBadRequest(InvalidDataException e) {
        return new ErrorBody(e.getStatus(), List.of("error on try add / update entity"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBody handleNotFound(NotFoundException e) {
        return new ErrorBody(e.getStatus(), List.of("data not found"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorBody handleConflict(ConflictException e) {
        return new ErrorBody(e.getStatus(), List.of("conflict data unique"));
    }
}
