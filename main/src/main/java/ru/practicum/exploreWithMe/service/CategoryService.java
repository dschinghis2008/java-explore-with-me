package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Category;

import java.util.List;

public interface CategoryService {
    Category add(Category category);

    Category update(Category category);

    Category getById(long id);

    List<Category> getAll();

    void remove(long id);
}
