package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.dto.validation.Create;
import ru.practicum.exploreWithMe.model.dto.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank(groups = {Update.class, Create.class})
    @Size(max = 1000)
    private String text;

    @NotNull(groups = {Create.class})
    private Long author;

    @NotNull(groups = {Create.class})
    private Long event;
}
