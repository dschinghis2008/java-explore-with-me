package ru.practicum.exploreWithMe.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Compilation;
import ru.practicum.exploreWithMe.model.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.model.dto.CompilationDto;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

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
        return compilation;
    }

    public CompilationDto toDtoFromNewDto(NewCompilationDto compilationDto) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilationDto.getId());
        dto.setTitle(compilationDto.getTitle());
        dto.setPinned(compilationDto.getPinned());
        return dto;
    }
}
