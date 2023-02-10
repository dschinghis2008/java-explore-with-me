package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Comment;

import java.util.List;

public interface CommentService {

    Comment add(long userId, Comment comment);

    Comment update(long authorId, Comment comment);

    Comment getById(long id);

    List<Comment> getAllByEventId(long eventId, int from, int size);

    List<Comment> getAllByAuthorId(long userId, int from, int size);

    Comment updVisible(long userId, boolean visible, boolean isAdm, Comment comment);

    void remove(long commentId, long authorId, boolean isAdm);
}
