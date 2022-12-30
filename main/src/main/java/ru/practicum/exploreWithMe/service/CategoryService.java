package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Category;

import java.util.Collection;

public interface CategoryService {
    Category add(Category category);

    Category update(Category category);

    Category getById(Integer id);

    Collection<Category> getAll();

    void remove(Integer id);
}
