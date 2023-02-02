package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.dto.CommentDto;
import ru.practicum.exploreWithMe.model.dto.CommentOutDto;
import ru.practicum.exploreWithMe.model.dto.validation.Create;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/comments/users")
public class CommentPrivateController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public CommentOutDto add(@RequestBody @Validated(Create.class) CommentDto dto) {
        log.info("--==>>COMMENT_REST dto=/{}/", dto);
        Comment comment = commentMapper.toComment(dto);
        log.info("--==>>COMMENT_REST comment(text,author,event)=/{},{},{}/",
                comment.getText(),comment.getAuthor().getId(),comment.getEvent().getId());
        return commentMapper.toOutDto(commentService.add(comment));
    }

    @PatchMapping("/{commentId}/user/{authorId}")
    public CommentOutDto updateText(@PathVariable long commentId, @PathVariable long authorId, @RequestParam String text) {
        return commentMapper.toOutDto(commentService.update(commentId, authorId, text));
    }

    @PatchMapping("/{commentId}")
    public CommentOutDto updVisible(@PathVariable long commentId,
                                 @RequestParam long userId, @RequestParam boolean visible) {
        return commentMapper.toOutDto(commentService.updVisible(commentId, userId, visible, false));
    }

    @DeleteMapping("/{commentId}/user/{authorId}")
    public void remove(@PathVariable long commentId, @PathVariable long authorId) {
        commentService.remove(commentId, authorId, false);
    }
}
