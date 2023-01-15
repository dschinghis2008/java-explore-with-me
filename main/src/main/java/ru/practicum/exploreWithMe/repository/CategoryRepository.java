package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select count(c) from Category c where c.name=:name")
    Long getCountByName(String name);

    @Query("select c from Category c")
    Page<Category> getAll(Integer from, Integer size, Pageable pageable);
}
