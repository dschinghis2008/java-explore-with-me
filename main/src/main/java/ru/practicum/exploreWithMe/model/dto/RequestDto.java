package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.Status;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    private LocalDateTime created;
    private Status status;
    private Long requester;
    private Long event;
}
