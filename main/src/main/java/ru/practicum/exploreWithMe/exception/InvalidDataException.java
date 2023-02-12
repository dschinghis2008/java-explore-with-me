package ru.practicum.exploreWithMe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDataException extends ResponseStatusException {
    public InvalidDataException(HttpStatus httpStatus) {
        super(httpStatus);
    }
}
