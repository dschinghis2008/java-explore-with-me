package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class NewCompilationDto {
    private Integer id;
    private String title;
    private Boolean pinned;
    private Collection<Integer> events;
}
