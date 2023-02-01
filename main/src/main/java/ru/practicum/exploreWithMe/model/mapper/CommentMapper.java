package ru.practicum.exploreWithMe.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.model.dto.CommentDto;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setEvent(comment.getEvent().getId());
        return commentDto;
    }

    public Comment toComment(CommentDto commentDto){
        Comment comment = new Comment();
        Event event = new Event();
        User user = new User();
        event.setId(commentDto.getEvent());
        user.setId(commentDto.getAuthor());
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        return comment;
    }
}
