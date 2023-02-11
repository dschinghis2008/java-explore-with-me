package ru.practicum.exploreWithMe.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.model.dto.CategoryDto;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public Category toCategory(Long id, CategoryDto categoryDto) {
        Category category = new Category();
        if (id != null) {
            category.setId(id);
        } else {
            category.setId(categoryDto.getId());
        }
        category.setName(categoryDto.getName());
        return category;
    }
}
