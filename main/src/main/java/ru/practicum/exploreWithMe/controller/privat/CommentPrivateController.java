package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.dto.CommentDto;
import ru.practicum.exploreWithMe.model.dto.CommentOutDto;
import ru.practicum.exploreWithMe.model.dto.validation.Create;
import ru.practicum.exploreWithMe.model.dto.validation.Update;
import ru.practicum.exploreWithMe.model.mapper.CommentMapper;
import ru.practicum.exploreWithMe.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/comments/private")
public class CommentPrivateController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public CommentOutDto add(@RequestBody @Validated(Create.class) CommentDto dto) {
        log.info("--==>>COMMENT_REST dto=/{}/", dto);
        Comment comment = commentMapper.toComment(dto);
        log.info("--==>>COMMENT_REST comment(text,author,event)=/{},{},{}/",
                comment.getText(), comment.getAuthor().getId(), comment.getEvent().getId());
        return commentMapper.toOutDto(commentService.add(comment));
    }

    @PatchMapping("/user/{authorId}")
    public CommentOutDto updateText(@PathVariable long authorId,
                                    @RequestBody @Validated({Update.class,Create.class}) CommentDto dto) {
        return commentMapper.toOutDto(commentService.update(authorId, commentMapper.toComment(dto)));
    }

    @PatchMapping("/user/{userId}/visible")
    public CommentOutDto updVisible(@PathVariable long userId, @RequestParam boolean visible,
                                    @RequestBody CommentDto dto) {
        return commentMapper.toOutDto(commentService.updVisible(userId, visible, false,
                commentMapper.toComment(dto)));
    }

    @GetMapping("/user/{authorId}")
    public List<CommentOutDto> getAllOfAuthor(@PathVariable long authorId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size){
        return commentService.getAllByAuthorId(authorId, from, size).stream()
                .map(commentMapper::toOutDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{commentId}/user/{authorId}")
    public void remove(@PathVariable long commentId, @PathVariable long authorId) {
        commentService.remove(commentId, authorId, false);
    }
}
