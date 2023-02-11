package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.CompilationInDto;

import java.util.List;

public interface CompilationService {
    CompilationDto add(CompilationInDto dto);

    Compilation getById(long id);

    List<Compilation> getAll(Boolean pinned, Integer from, Integer size);

    Compilation update(long id, CompilationInDto dto);

    void addEventToCompilation(long idEvent, long idComp);

    void deleteEventFromCompilation(long idEvent, long idComp);

    void deleteCompilation(long id);
}
