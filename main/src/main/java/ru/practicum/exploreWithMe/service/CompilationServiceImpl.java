package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.model.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.repository.CompilationRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto add(NewCompilationDto dto) {
        Compilation compilation = compilationMapper.toCompilation(dto);
        Set<Event> events = eventRepository.getEventsById(dto.getEvents());
        compilation.setEvents(events);
        log.info("---===>>> added compilation /{}/", dto);
        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public Compilation getById(long id) {
        log.info("---===>>> query compilation /{}/", id);
        return compilationRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("---===>>> query compilations from=/{}/, size=/{}/, pinned=/{}/", from, size, pinned);
        return compilationRepository.getAll(pinned, pageable).getContent();
    }

    @Override
    public Compilation pin(long id) {
        Compilation compilation =
                compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.setPinned(true);
        log.info("---===>>> pin compilation id=/{}/", id);
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation unpin(long id) {
        Compilation compilation =
                compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.setPinned(false);
        log.info("---===>>> unpin compilation id=/{}/", id);
        return compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(long idEvent, long idComp) {
        Compilation compilation = compilationRepository.findById(idComp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.getEvents().add(event);
        log.info("---===>>> add event id=/{}/ to compilation id=/{}/", idEvent, idComp);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteEventFromCompilation(long idEvent, long idComp) {
        log.info("---===>>> COMP_SERV delete event from comp");
        Compilation compilation = compilationRepository.findById(idComp)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event eventResult = new Event();
        for (Event event : compilation.getEvents()) {
            if (event.getId() == idEvent) {
                eventResult = event;
            }
        }
        compilation.getEvents().remove(eventResult);
        log.info("---===>>> deleted event id=/{}/ from compilation id=/{}/", idEvent, idComp);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(long id) {
        log.info("---===>>> delete compilation id=/{}/", id);
        compilationRepository.deleteById(id);
    }
}
