package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.service.CategoryService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    public Category add(@RequestBody Category category) {
        return categoryService.add(category);
    }

    @PatchMapping("/admin/categories")
    public Category update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void remove(@PathVariable Integer catId) {
        categoryService.remove(catId);
    }

    @GetMapping("/categories")
    public Collection<Category> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/categories/{catId}")
    public Category getById(@PathVariable Integer catId) {
        return categoryService.getById(catId);
    }

}
