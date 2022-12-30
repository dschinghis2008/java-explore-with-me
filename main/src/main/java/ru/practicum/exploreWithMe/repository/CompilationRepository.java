package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    @Query("select c from Compilation c where c.pinned = :pinned")
    Page<Compilation> getAll(Boolean pinned, Pageable pageable);
}
