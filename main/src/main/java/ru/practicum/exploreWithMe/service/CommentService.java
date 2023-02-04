package ru.practicum.exploreWithMe.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.dto.CommentDto;

import java.util.List;

public interface CommentService {
    Comment add(Comment comment);
    Comment update(long authorId, Comment comment);
    Comment getById(long id);
    List<Comment> getAllByEventId(long eventId, int from, int size);
    List<Comment> getAllByAuthorId(long userId, int from, int size);
    Comment updVisible(long commentId, long userId, boolean visible, boolean isAdm);
    void remove(long commentId, long authorId, boolean isAdm);
}
