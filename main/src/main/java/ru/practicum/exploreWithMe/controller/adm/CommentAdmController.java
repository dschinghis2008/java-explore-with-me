package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CommentDto;
import ru.practicum.exploreWithMe.model.dto.CommentOutDto;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/adm")
public class CommentAdmController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PatchMapping("/visible")
    public CommentOutDto updVisible(@RequestParam boolean visible, @RequestBody CommentDto dto) {
        return commentMapper.toOutDto(commentService.updVisibleAdm(visible, commentMapper.toComment(dto)));
    }

    @DeleteMapping("/{commentId}")
    public void remove(@PathVariable long commentId) {
        commentService.removeByAdmin(commentId);
    }
}
