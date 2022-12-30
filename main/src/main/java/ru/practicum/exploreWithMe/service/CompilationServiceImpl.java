package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.repository.CompilationRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Compilation add(Compilation compilation) {
        log.info("---===>>> added compilation /{}/", compilation);
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation getById(Integer id) {
        log.info("---===>>> query compilation /{}/", id);
        return compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Collection<Compilation> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("---===>>> query compilations from=/{}/, size=/{}/, pinned=/{}/", from, size, pinned);
        return compilationRepository.getAll(pinned, pageable).getContent();
    }

    @Override
    @Transactional
    public Compilation pin(Integer id) {
        Compilation compilation =
                compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.setPinned(true);
        log.info("---===>>> pin compilation id=/{}/", id);
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public Compilation unpin(Integer id) {
        Compilation compilation =
                compilationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        compilation.setPinned(false);
        log.info("---===>>> unpin compilation id=/{}/", id);
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Integer idEvent, Integer idComp) {
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
    public void deleteEventFromCompilation(Integer idEvent, Integer idComp) {
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
    public void deleteCompilation(Integer id) {
        log.info("---===>>> delete compilation id=/{}/", id);
        compilationRepository.deleteById(id);
    }
}
