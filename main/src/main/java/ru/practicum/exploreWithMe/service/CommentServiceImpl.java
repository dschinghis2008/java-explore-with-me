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
import ru.practicum.exploreWithMe.model.EventState;
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
    public Comment add(long userId, Comment comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findByIdAndState(comment.getEvent().getId(), EventState.PUBLISHED);
        if (event == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        comment.setAuthor(user);
        comment.setEvent(event);
        switch (commentRepository.foundRoleOfCommentator(user.getId(), event.getId())) {
            case 0:
            case 1:
            case 2:
                log.info("--==>>COMMENT_SRV ADD: Not found event or/and user");
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
    public Comment update(long authorId, Comment comment) {
        Comment commentUpd = commentRepository.findByIdAndAuthorId(comment.getId(), authorId);
        if (commentUpd == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        commentUpd.setText(comment.getText());
        log.info("--==>>COMMENT_SRV: update comment id=/{}/, text=/{}/", commentUpd.getId(), commentUpd.getText());
        return commentUpd;
    }

    private Comment updVisible(long userId, boolean visible, long commentId, boolean isAdm) {
        Comment commentUpd = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!isAdm) {
            if (commentRepository
                    .foundRoleOfCommentator(userId, commentUpd.getEvent().getId()) != 103) {
                log.info("--==>>COMMENT_SRV пользователь не является инициатором события ," +
                        " признак видимости коммента не может быть изменен");
                throw new NotFoundException(HttpStatus.NOT_FOUND);
            }
        }
        commentUpd.setVisible(visible);
        log.info("--==>>COMMENT_SERV comment id=/{}/ set visible=/{}/", commentUpd.getId(), visible);
        return commentUpd;
    }

    @Override
    @Transactional
    public Comment updVisibleUser(long userId, boolean visible, Comment comment) {
        return updVisible(userId, visible, comment.getId(), false);
    }

    @Override
    @Transactional
    public Comment updVisibleAdm(boolean visible, Comment comment) {
        return updVisible(0, visible, comment.getId(), true);
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
        return commentRepository.findAllByEventOrderByCreated(event, pageable).getContent();
    }

    @Override
    public List<Comment> getAllByAuthorId(long userId, int from, int size) {
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(from, size);
        log.info("--==>>COMMENT_SERV query all comments of user Id=/{}/", userId);
        return commentRepository.findAllByAuthorOrderByCreated(author, pageable).getContent();
    }

    @Override
    @Transactional
    public void removeByAuthor(long commentId, long authorId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, authorId);
        if (comment == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        commentRepository.deleteById(commentId);
        log.info("--==>>COMMENT_SRV delete comment /{}/", commentId);
    }

    @Override
    @Transactional
    public void removeByAdmin(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        commentRepository.deleteById(commentId);
        log.info("--==>>COMMENT_SRV delete comment /{}/", commentId);
    }
}
