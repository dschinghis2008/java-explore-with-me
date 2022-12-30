package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.Event;

import java.util.Collection;

@Data
public class CompilationDto {
    private Integer id;
    private String title;
    private Boolean pinned;
    private Collection<Event> events;
}
