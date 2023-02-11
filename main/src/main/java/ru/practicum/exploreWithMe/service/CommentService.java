package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Comment;

import java.util.List;

public interface CommentService {

    Comment add(long userId, Comment comment);

    Comment update(long authorId, Comment comment);

    Comment getById(long id);

    List<Comment> getAllByEventId(long eventId, int from, int size);

    List<Comment> getAllByAuthorId(long userId, int from, int size);

    Comment updVisibleUser(long userId, boolean visible, Comment comment);

    Comment updVisibleAdm(boolean visible, Comment comment);

    void removeByAuthor(long commentId, long authorId);

    void removeByAdmin(long commentId);

}
