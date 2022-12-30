package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Compilation;

import java.util.Collection;

public interface CompilationService {
    Compilation add(Compilation compilation);

    Compilation getById(Integer id);

    Collection<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation pin(Integer id);

    Compilation unpin(Integer id);

    void addEventToCompilation(Integer idEvent, Integer idComp);

    void deleteEventFromCompilation(Integer idEvent, Integer idComp);

    void deleteCompilation(Integer id);
}
