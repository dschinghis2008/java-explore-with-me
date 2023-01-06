package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.Status;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r where r.requester.id = :requester")
    Collection<Request> findAllByRequesterOrderByCreated(Long requester);

    @Query("select r from Request r where r.event.id = :eventId")
    Collection<Request> findAllOfAuthor(Long eventId);

    Collection<Request> findAllByEventId(Long eventId);

    Collection<Request> findAllByEventIdAndStatus(Long eventId, Status status);

    @Query("select count(r.id) from Request r where r.event.id=:eventId and r.status='CONFIRMED'")
    Integer getCountConfirmed(Long eventId);
}
