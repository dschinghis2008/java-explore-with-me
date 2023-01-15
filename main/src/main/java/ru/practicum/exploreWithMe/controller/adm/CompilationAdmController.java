package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.CompilationInDto;
import ru.practicum.exploreWithMe.model.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationAdmController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping("/admin/compilations")
    public CompilationDto add(@RequestBody @Valid CompilationInDto compilationDto) {
        log.info("====>>> COMP_REST add comp=/{}/", compilationDto);
        return compilationService.add(compilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public CompilationDto pin(@PathVariable long compId) {
        return compilationMapper.toDto(compilationService.pin(compId));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void delete(@PathVariable long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public CompilationDto unpin(@PathVariable long compId) {
        return compilationMapper.toDto(compilationService.unpin(compId));
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long eventId, @PathVariable long compId) {
        compilationService.addEventToCompilation(eventId, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable long compId, @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(eventId, compId);
    }


}
