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
    public CommentOutDto updVisible(@RequestParam boolean visible, @RequestBody CommentDto dto){
        return commentMapper.toOutDto(commentService.updVisible(0, visible, true,
                commentMapper.toComment(dto)));
    }

    @DeleteMapping("/{commentId}")
    public void remove(@PathVariable long commentId){
        commentService.remove(commentId, 0, true);
    }
}
