package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.dto.validation.Create;
import ru.practicum.exploreWithMe.model.dto.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {

    private Long id;

    @NotBlank(groups = {Create.class, Update.class})
    private String name;
}
