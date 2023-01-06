package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CategoryDto;
import ru.practicum.exploreWithMe.model.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.service.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryAdmController {
    private final CategoryService categoryService;
    private  final CategoryMapper categoryMapper;

    @PostMapping("/admin/categories")
    public CategoryDto add(@RequestBody CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryService.add(categoryMapper.toCategory(categoryDto)));
    }

    @PatchMapping("/admin/categories")
    public CategoryDto update(@RequestBody CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryService.add(categoryMapper.toCategory(categoryDto)));
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void remove(@PathVariable Long catId) {
        categoryService.remove(catId);
    }

}
