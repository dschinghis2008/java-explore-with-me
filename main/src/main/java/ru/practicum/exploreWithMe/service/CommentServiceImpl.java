package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.CommentRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Comment add(Comment comment) {
        User user = userRepository.findById(comment.getAuthor().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(comment.getEvent().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        comment.setAuthor(user);
        comment.setEvent(event);
        switch (commentRepository.foundRoleOfCommentator(user.getId(), event.getId())) {
            case 0:
            case 1:
            case 2:
                log.info("--==>>COMMENT_SRV: Not found event or/and user");
                throw new NotFoundException(HttpStatus.NOT_FOUND);
            case 3:
                comment.setVisible(false);
                break;
            case 13:
            case 103:
                comment.setVisible(true);
        }
        comment.setCreated(LocalDateTime.now());
        log.info("--==>>COMMENT_SRV: added comment /{}/", comment.getId());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(long commentId, long authorId, String text) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        comment.setText(text);
        log.info("--==>>COMMENT_SRV: update comment id=/{}/, text=/{}/", commentId, text);
        return comment;
    }

    @Override
    @Transactional
    public Comment updVisible(long commentId, long userId, boolean visible, boolean isAdm) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!isAdm) {
            if (commentRepository
                    .foundRoleOfCommentator(userId, comment.getEvent().getId()) != 103) {
                log.info("--==>>COMMENT_SRV пользователь не является инициатором события и запрос " +
                        "не от администратора, признак видимости коммента не может быть изменен");
                throw new NotFoundException(HttpStatus.NOT_FOUND);
            }
        }
        comment.setVisible(visible);
        log.info("--==>>COMMENT_SERV coment id=/{}/ set visible=/{}/", commentId, visible);
        return comment;
    }

    @Override
    public Comment getById(long id) {
        log.info("--==>>COMMENT_SERV query comment id=/{}/", id);
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Comment> getAllByEventId(long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(from, size);
        log.info("--==>>COMMENT_SERV query all comments on eventId=/{}/", eventId);
        return commentRepository.findAllByEvent(event, pageable).getContent();
    }

    @Override
    @Transactional
    public void remove(long commentId, long authorId, boolean isAdm) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!isAdm) {
            if (!comment.getAuthor().getId().equals(authorId)) {
                log.info("--==>>COMMENT_SRV пользователь не является автором коммента и запрос " +
                        "не от администратора, коммент не может быть удален");
                throw new NotFoundException(HttpStatus.NOT_FOUND);
            }
        }
        commentRepository.deleteById(commentId);
    }
}
