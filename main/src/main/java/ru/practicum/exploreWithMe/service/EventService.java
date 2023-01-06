package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.EventDto;
import ru.practicum.exploreWithMe.model.dto.EventShortDto;
import ru.practicum.exploreWithMe.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    Event add(long userId, Event event);

    Event publish(long eventId);

    Event reject(long eventId);

    Collection<Event> getEventsAdm(Long[] usersId, String[] states, Long[] catId,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsPublic(String text, Integer[] category, Boolean paid,
                                        LocalDateTime dt1, LocalDateTime dt2, String sort, Integer from, Integer size,
                                        HitDto hitDto);

    EventDto getById(long id, HitDto hitDto);

    Event getById(long id);

    Collection<Event> getByUser(long id, Integer from, Integer size);

    Event update(long userId, Event event);

    Event updateAdm(long id, Event event);

    Event getUserEvent(long userId, long eventId);

    Event cancelEvent(long userId, long eventId);
}
