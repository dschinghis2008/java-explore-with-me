package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class NewCompilationDto {
    private Long id;

    @NotNull
    private String title;

    private Boolean pinned;
    private Set<Long> events;
}
