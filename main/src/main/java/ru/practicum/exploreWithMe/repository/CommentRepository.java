package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.Comment;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true, value =
            "select sum(usr + participant + initiator + event) from -- 2, 1 or 0 - not found event or user or both,\n" +
                    "(-- 3-user, 13-participant, 103 - initiator of the event\n" +
                    "    select\n" +
                    "       u1 usr,\n" +
                    "       (case u4 when 1 then 2 else 0 end) event,\n" +
                    "       (case u2 when 1 then 10 else 0 end) participant,\n" +
                    "       (case u3 when 1 then 100 else 0 end) initiator\n" +
                    "    from\n" +
                    "    (\n" +
                    "        select count(id) u1, 0 u2, 0 u3, 0 u4  from users where id=:userId\n" +
                    "        union\n" +
                    "        select 0 u1, 0 u2, count(id) u3, 0 u4 from events e where e.initiator_id=:userId " +
                    "        and e.id=:eventId and e.state='PUBLISHED'\n" +
                    "        union\n" +
                    "        select 0 u1, count(id) u2, 0 u3, 0 u4 from requests r where requester_id=:userId and " +
                    "        r.event_id=:eventId and r.status='CONFIRMED'\n" +
                    "        union\n" +
                    "        select 0 u1, 0 u2, 0 u3, count(id) u4 from events e " +
                    "        where e.id=:eventId and e.state='PUBLISHED') a1) a2")
    int foundRoleOfCommentator(Long userId, Long eventId);

    Page<Comment> findAllByEventOrderByCreated(Event event, Pageable pageable);

    Page<Comment> findAllByAuthorOrderByCreated(User user, Pageable pageable);

    @Query("select c from Comment c where c.id=:id and c.author.id=:authorId")
    Comment findByIdAndAuthorId(long id, long authorId);
}
