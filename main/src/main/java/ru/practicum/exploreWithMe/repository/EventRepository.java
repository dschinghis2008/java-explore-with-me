package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.State;
import ru.practicum.exploreWithMe.model.User;

import java.time.LocalDateTime;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e where e.initiator.id in :usersId and e.state in :states and e.category.id in :catId" +
            " and e.eventDate between :dt1 and :dt2 order by e.eventDate")
    Page<Event> getEventsAdm(Long[] usersId, State[] states, Long[] catId,
                             LocalDateTime dt1, LocalDateTime dt2, Pageable pageable);

    @Query("select e from Event e where e.state in :states and e.eventDate between :dt1 and :dt2" +
            " order by e.eventDate desc")
    Page<Event> getEventsAdmAll(State[] states, LocalDateTime dt1, LocalDateTime dt2, Pageable pageable);

    @Query("select e from Event e where e.initiator.id in :usersId and e.category.id in :catgsId " +
            "order by e.eventDate desc")
    Page<Event> getAllByInitiatorAndCatg(Long[] usersId, Long[] catgsId, Pageable pageable);

    @Query("select e from Event e where e.state='PUBLISHED' and e.eventDate between :dt1 and :dt2 " +
            "and e.category in :category and e.paid = :paid " +
            "and (lower(e.annotation) like concat('%', lower(:text), '%') " +
            "or lower(e.description) like concat('%', lower(:text), '%')) order by e.id desc")
    Page<Event> getEventsPublic(String text, Long[] category, Boolean paid,
                                LocalDateTime dt1, LocalDateTime dt2, Pageable pageable);

    @Query("select e from Event e where (lower(e.annotation) like concat('%', lower(:text), '%') " +
            "or lower(e.description) like concat('%', lower(:text), '%')) and e.paid = :paid")
    Page<Event> getEventsPublicByDescrAndPaid(String text, Boolean paid, Pageable pageable);

    @Query("select e from Event e where e.state='PUBLISHED' and e.eventDate between :dt1 and :dt2 " +
            "and e.paid = :paid " +
            "and (lower(e.annotation) like concat('%', lower(:text), '%') " +
            "or lower(e.description) like concat('%', lower(:text), '%')) order by e.id desc")
    Page<Event> getEventsPublicAllCategSortByDate(String text, Boolean paid,
                                                  LocalDateTime dt1, LocalDateTime dt2, Pageable pageable);

    @Query("select e from Event e where e.state='PUBLISHED' and e.eventDate between :dt1 and :dt2 and e.paid=:paid" +
            " order by e.id desc")
    Page<Event> getEventsPublicAllWithDate(Boolean paid, LocalDateTime dt1, LocalDateTime dt2, Pageable pageable);

    @Query("select e from Event e where e.state='PUBLISHED' order by e.eventDate")
    Page<Event> getPublicAll(Pageable pageable);

    Page<Event> findAllByInitiatorId(Long id, Pageable pageable);

    Event findByIdAndInitiator(Long id, User initiator);

    Event findByIdAndInitiatorId(Long id, Long initiatorId);

    @Query("select e from Event e where e.id in :ids")
    Set<Event> getEventsById(Set<Long> ids);
}
