package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CategoryDto;
import ru.practicum.exploreWithMe.model.dto.validation.Create;
import ru.practicum.exploreWithMe.model.dto.validation.Update;
import ru.practicum.exploreWithMe.model.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.service.CategoryService;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryAdmController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDto> add(@RequestBody @Validated(Create.class) CategoryDto categoryDto) {
        return new ResponseEntity<>(
                categoryMapper.toDto(categoryService.add(categoryMapper.toCategory(null, categoryDto))),
                HttpStatus.CREATED);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto update(@PathVariable long catId, @RequestBody @Validated(Update.class) CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryService.update(categoryMapper.toCategory(catId, categoryDto)));
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable long catId) {
        categoryService.remove(catId);
    }

}
