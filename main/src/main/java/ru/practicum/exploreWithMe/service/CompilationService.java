package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {
    CompilationDto add(NewCompilationDto dto);

    Compilation getById(Long id);

    Collection<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation pin(Long id);

    Compilation unpin(Long id);

    void addEventToCompilation(Long idEvent, Long idComp);

    void deleteEventFromCompilation(Long idEvent, Long idComp);

    void deleteCompilation(Long id);
}
