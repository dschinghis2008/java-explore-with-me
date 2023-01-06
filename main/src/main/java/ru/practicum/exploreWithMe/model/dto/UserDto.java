package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;
}