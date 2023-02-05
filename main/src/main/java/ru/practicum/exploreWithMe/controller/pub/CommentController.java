package ru.practicum.exploreWithMe.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CommentOutDto;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Validated
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{id}")
    public CommentOutDto getById(@PathVariable long id) {
        return commentMapper.toOutDto(commentService.getById(id));
    }

    @GetMapping
    public List<CommentOutDto> getAllByEvent(@RequestParam long eventId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getAllByEventId(eventId, from, size).stream()
                .map(commentMapper::toOutDto)
                .collect(Collectors.toList());
    }
}
