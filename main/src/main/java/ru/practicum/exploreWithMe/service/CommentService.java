package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;

import java.util.List;

public interface CommentService {
    Comment add(User user, Event event);
    Comment update(Comment comment, Long id);
    Comment getById(Long id);
    List<Comment> getAllByEventId(Long eventId);
    Comment updVisible(Long id);
    void remove(Long id);
}
