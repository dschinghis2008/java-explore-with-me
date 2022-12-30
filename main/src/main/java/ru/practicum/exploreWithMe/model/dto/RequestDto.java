package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.Status;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Integer id;
    private LocalDateTime created;
    private Status status;
    private Integer requester;
    private Integer event;
}
