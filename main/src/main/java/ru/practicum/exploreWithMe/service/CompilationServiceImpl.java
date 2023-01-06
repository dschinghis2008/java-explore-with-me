package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.model.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.repository.CompilationRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto dto) {
        Compilation compilation = compilationMapper.toCompilation(dto);
        Set<Event> events = eventRepository.getEventsById(dto.getEvents());
        compilation.setEvents(events);
        log.info("---===>>> added compilation /{}/", dto);
        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public Compilation getById(Long id) {
        log.info("---===>>> query compilation /{}/", id);
        return compilationRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Collection<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("---===>>> query compilations from=/{}/, size=/{}/, pinned=/{}/", from, size, pinned);
        return compilationRepository.getAll(pinned, pageable).getContent();
    }

    @Override
    @Transactional
    public Compilation pin(Long id) {
        Compilation compilation =
                compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.setPinned(true);
        log.info("---===>>> pin compilation id=/{}/", id);
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public Compilation unpin(Long id) {
        Compilation compilation =
                compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.setPinned(false);
        log.info("---===>>> unpin compilation id=/{}/", id);
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long idEvent, Long idComp) {
        Compilation compilation = compilationRepository.findById(idComp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.getEvents().add(event);
        log.info("---===>>> add event id=/{}/ to compilation id=/{}/", idEvent, idComp);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long idEvent, Long idComp) {
        log.info("---===>>> COMP_SERV delete event from comp");
        Compilation compilation = compilationRepository.findById(idComp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Event eventResult = new Event();
        for (Event event : compilation.getEvents()) {
            if (event.getId().equals(idEvent)) {
                eventResult = event;
            }
        }
        compilation.getEvents().remove(eventResult);
        log.info("---===>>> deleted event id=/{}/ from compilation id=/{}/", idEvent, idComp);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        log.info("---===>>> delete compilation id=/{}/", id);
        compilationRepository.deleteById(id);
    }
}
