package ru.practicum.exploreWithMe.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@RestControllerAdvice("ru.practicum.exploreWithMe.controller")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBody handleBadRequest(InvalidDataException e) {
        log.info("--==>>REST BadRequest exception: /{}/", e.getMessage());
        return new ErrorBody(e.getStatus(), List.of("error on try add / update entity"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBody handleNotFound(NotFoundException e) {
        log.info("--==>>REST NotFound exception: /{}/", e.getMessage());
        return new ErrorBody(e.getStatus(), List.of("data not found"));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorBody handleConflict(ConflictException e) {
        log.info("--==>>REST Conflict exception: /{}/", e.getMessage());
        return new ErrorBody(e.getStatus(), List.of("conflict data unique"));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorBody handleUnchecked(Throwable e) {
        log.info("--==>>REST Unchecked exception: INTERNAL_SERVER_ERROR");
        return new ErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, List.of("server error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBody handleArgsNotValid(MethodArgumentNotValidException e) {
        log.info("--==>>REST ArgsNotValid exception: /{}/", e.getMessage());
        return new ErrorBody(HttpStatus.BAD_REQUEST, List.of("not valid data"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBody handleConstraintViolation(ConstraintViolationException e) {
        log.info("--==>>REST ConstrViolation exception: /{}/", e.getMessage());
        return new ErrorBody(HttpStatus.BAD_REQUEST, List.of("not valid data"));
    }
}
