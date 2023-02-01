package ru.practicum.exploreWithMe.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.exploreWithMe.model.Comment;

import java.util.List;

public interface CommentService {
    Comment add(Comment comment);
    Comment update(long commentId, long authorId,  String text);
    Comment getById(long id);
    List<Comment> getAllByEventId(long eventId, int from, int size);
    Comment updVisible(long commentId, long userId, boolean visible, boolean isAdm);
    void remove(long commentId, long authorId, boolean isAdm);
}
