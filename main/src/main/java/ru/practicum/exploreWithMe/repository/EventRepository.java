package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.User;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor {

    Page<Event> findAllByInitiatorId(Long id, Pageable pageable);

    Event findByIdAndInitiator(Long id, User initiator);

    Event findByIdAndInitiatorId(Long id, Long initiatorId);

    @Query("select e from Event e where e.id in :ids")
    Set<Event> getEventsById(Set<Long> ids);

}
