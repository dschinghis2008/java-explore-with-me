package ru.practicum.exploreWithMe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException  extends ResponseStatusException {
    public ConflictException(HttpStatus httpStatus) {
        super(httpStatus);
    }
}
