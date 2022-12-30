package ru.practicum.exploreWithMe.model.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.service.EventService;

import java.util.ArrayList;
import java.util.Collection;

@Component
@AllArgsConstructor
@Slf4j
public class CompilationMapper {
    private final EventService eventService;

    public CompilationDto toDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(compilation.getEvents());
        return compilationDto;
    }

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setId(compilationDto.getId());
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        Collection<Event> events = new ArrayList<>();
        for (Integer id : compilationDto.getEvents()) {
            log.info("====>>>> COMP_MAPPER add Event id=/{}/, getEvents=/{}/", id, compilation.getEvents());
            events.add(eventService.getById(id));
        }
        compilation.setEvents(events);
        return compilation;
    }
}
