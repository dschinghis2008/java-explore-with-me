package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;
import ru.practicum.exploreWithMe.model.dto.CompilationInDto;
import ru.practicum.exploreWithMe.model.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationAdmController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> add(@RequestBody @Valid CompilationInDto compilationDto) {
        log.info("====>>> COMP_REST add comp=/{}/", compilationDto);
        return new ResponseEntity<>(compilationService.add(compilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@PathVariable long compId, @RequestBody CompilationInDto dto) {
        return compilationMapper.toDto(compilationService.update(compId, dto));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        compilationService.deleteCompilation(compId);
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
