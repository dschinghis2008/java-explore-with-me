package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.Status;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r from Request r where r.id in :ids and r.status=:status")
    List<Request> findAllByIdsAndStatus(List<Long> ids, Status status);

    @Query("select r from Request r where r.requester.id = :requesterId and r.id = :id")
    Request findByRequesterIdAndId(Long requesterId, Long id);

    @Query("select r from Request r where r.requester.id = :requesterId and r.event.id = :eventId")
    Request findByRequesterIdAndEventId(Long requesterId, Long eventId);

    @Query("select r from Request r where r.requester.id = :requester")
    List<Request> findAllByRequesterOrderByCreated(Long requester, Pageable pageable);

    @Query("select r from Request r where r.id in :ids")
    List<Request> findAllByIds(List<Long> ids);

    List<Request> findAllByEventId(Long eventId, Pageable pageable);

    List<Request> findAllByEventIdAndStatus(Long eventId, Status status);

    @Query("select count(r.id) from Request r where r.event.id=:eventId and r.status='CONFIRMED'")
    Integer getCountConfirmed(Long eventId);
}
