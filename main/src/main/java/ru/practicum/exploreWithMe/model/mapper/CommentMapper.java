package ru.practicum.exploreWithMe.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.dto.CommentDto;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public CommentDto toDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentDto.getId());
        commentDto.setText(commentDto.getText());
        commentDto.setAuthor(userMapper.toDto(comment.getAuthor()));
        commentDto.setEvent(eventMapper.toDto(comment.getEvent()));
        return commentDto;
    }

    public Comment toComment(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setAuthor(userMapper.toUser(commentDto.getAuthor()));
        comment.setEvent(eventMapper.toEvent(commentDto.getEvent()));
        return comment;
    }
}
