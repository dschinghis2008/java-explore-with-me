package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CommentDto;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/adm")
public class CommentAdmController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PatchMapping("/{commentId}")
    public CommentDto updVisible(@PathVariable long commentId, @RequestParam boolean visible){
        return commentMapper.toDto(commentService.updVisible(commentId, 0, visible, true));
    }

    @DeleteMapping("/{commentId}")
    public void remove(@PathVariable long commentId){
        commentService.remove(commentId, 0, true);
    }
}
