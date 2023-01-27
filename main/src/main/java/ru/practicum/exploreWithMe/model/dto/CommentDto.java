package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.dto.validation.Create;
import ru.practicum.exploreWithMe.model.dto.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDto {
    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank(groups = {Update.class, Create.class})
    private String text;

    @NotBlank(groups = {Create.class})
    private UserDto author;

    @NotBlank(groups = {Create.class})
    private EventDto event;
}
