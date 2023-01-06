package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.Event;

import java.util.Collection;
import java.util.Set;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private Set<Event> events;
}
