package ru.practicum.exploreWithMe.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventInDto {

    private Long eventId;
    private String annotation;
    private String title;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;

    @NotNull
    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull
    private Long category;

    private UserDto initiator;
}
