package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl  implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment add(User user, Event event) {
        Comment comment = new Comment();

        switch (commentRepository.foundRoleOfCommentator(user.getId(), event.getId())){
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
        comment.setAuthor(user);
        comment.setEvent(event);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(Comment comment, Long id) {
        return null;
    }

    @Override
    @Transactional
    public Comment updVisible(Long id) {
        return null;
    }

    @Override
    public Comment getById(Long id) {
        return null;
    }

    @Override
    public List<Comment> getAllByEventId(Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        commentRepository.deleteById(id);
    }
}
