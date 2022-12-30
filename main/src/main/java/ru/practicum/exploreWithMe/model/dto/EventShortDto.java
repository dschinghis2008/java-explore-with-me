package ru.practicum.exploreWithMe.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.exploreWithMe.model.User;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private Integer id;
    private String annotation;
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Integer category;
    private Boolean paid;
    private User initiator;
    private Integer views;
    private Integer confirmedRequests;
}
