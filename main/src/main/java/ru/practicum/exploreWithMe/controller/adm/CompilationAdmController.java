package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.model.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.service.CompilationService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationAdmController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping("/admin/compilations")
    public CompilationDto add(@RequestBody NewCompilationDto compilationDto) {
        if (compilationDto.getTitle() == null) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        log.info("====>>> COMP_REST add comp=/{}/", compilationDto);
        return compilationService.add(compilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public CompilationDto pin(@PathVariable Long compId) {
        return compilationMapper.toDto(compilationService.pin(compId));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void delete(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public CompilationDto unpin(@PathVariable Long compId) {
        return compilationMapper.toDto(compilationService.unpin(compId));
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long eventId, @PathVariable Long compId) {
        compilationService.addEventToCompilation(eventId, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(eventId, compId);
    }


}
