package ru.practicum.exploreWithMe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Getter
public class ErrorBody {
    private final Integer status;
    private final Collection<String> errors;

    public ErrorBody(HttpStatus httpStatus, Collection<String> errors) {
        this.status = httpStatus.value();
        this.errors = errors;
    }
}
