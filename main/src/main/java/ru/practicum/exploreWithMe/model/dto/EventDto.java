package ru.practicum.exploreWithMe.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.exploreWithMe.model.State;
import ru.practicum.exploreWithMe.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String annotation;
    private String title;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    private Integer participantLimit;
    private Boolean requestModeration;
    private State state;

    @NotNull
    private Long category;

    private User initiator;
    private Integer views;
    private Integer confirmedRequests;
}
