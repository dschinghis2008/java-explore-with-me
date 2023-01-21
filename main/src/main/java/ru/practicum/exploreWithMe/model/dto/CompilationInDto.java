package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class CompilationInDto {

    @NotBlank
    private String title;

    private boolean pinned;
    private Set<Long> events;

}
