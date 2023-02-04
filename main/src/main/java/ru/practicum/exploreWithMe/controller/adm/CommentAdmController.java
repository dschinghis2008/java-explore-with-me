package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.CommentOutDto;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/adm")
public class CommentAdmController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PatchMapping("/{commentId}/visible")
    public CommentOutDto updVisible(@PathVariable long commentId, @RequestParam boolean visible){
        return commentMapper.toOutDto(commentService.updVisible(commentId, 0, visible, true));
    }

    @DeleteMapping("/{commentId}")
    public void remove(@PathVariable long commentId){
        commentService.remove(commentId, 0, true);
    }
}
