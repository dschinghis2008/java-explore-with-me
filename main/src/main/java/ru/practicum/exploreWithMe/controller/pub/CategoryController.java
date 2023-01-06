package ru.practicum.exploreWithMe.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CategoryDto;
import ru.practicum.exploreWithMe.model.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.service.CategoryService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private  final CategoryMapper categoryMapper;

    @GetMapping("/categories")
    public Collection<CategoryDto> getAll() {
        return categoryService.getAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        return categoryMapper.toDto(categoryService.getById(catId));
    }

}
