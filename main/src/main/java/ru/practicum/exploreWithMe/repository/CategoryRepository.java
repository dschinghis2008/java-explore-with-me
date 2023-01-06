package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select count(c) from Category c where c.name=:name")
    Long getCountByName(String name);
}
