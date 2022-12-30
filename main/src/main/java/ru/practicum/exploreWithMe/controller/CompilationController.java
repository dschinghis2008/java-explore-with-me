package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.model.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping("/admin/compilations")
    public CompilationDto add(@RequestBody NewCompilationDto compilationDto) {
        if (compilationDto.getTitle() == null) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        log.info("====>>> COMP_REST add comp=/{}/", compilationDto);
        return compilationMapper.toDto(compilationService.add(compilationMapper.toCompilation(compilationDto)));
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public CompilationDto pin(@PathVariable Integer compId) {
        return compilationMapper.toDto(compilationService.pin(compId));
    }

    @GetMapping("/compilations")
    public Collection<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        Collection<Compilation> compilations = compilationService.getAll(pinned, from, size);
        Collection<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            result.add(compilationMapper.toDto(compilation));
        }
        return result;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getById(@PathVariable Integer compId) {
        return compilationMapper.toDto(compilationService.getById(compId));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void delete(@PathVariable Integer compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public CompilationDto unpin(@PathVariable Integer compId) {
        return compilationMapper.toDto(compilationService.unpin(compId));
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Integer eventId, @PathVariable Integer compId) {
        compilationService.addEventToCompilation(eventId, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Integer compId, @PathVariable Integer eventId) {
        compilationService.deleteEventFromCompilation(eventId, compId);
    }


}
