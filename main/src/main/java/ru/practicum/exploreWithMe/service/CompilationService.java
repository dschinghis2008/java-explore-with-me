package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {
    CompilationDto add(NewCompilationDto dto);

    Compilation getById(long id);

    Collection<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation pin(long id);

    Compilation unpin(long id);

    void addEventToCompilation(long idEvent, long idComp);

    void deleteEventFromCompilation(long idEvent, long idComp);

    void deleteCompilation(long id);
}
