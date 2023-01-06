package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class NewCompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private Set<Long> events;
}
