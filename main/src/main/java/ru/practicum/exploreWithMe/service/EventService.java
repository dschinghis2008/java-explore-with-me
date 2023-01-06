package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.EventDto;
import ru.practicum.exploreWithMe.model.dto.EventShortDto;
import ru.practicum.exploreWithMe.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    Event add(Long userId, Event event);

    Event publish(Long eventId);

    Event reject(Long eventId);

    Collection<Event> getEventsAdm(Long[] usersId, String[] states, Long[] catId,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsPublic(String text, Integer[] category, Boolean paid,
                                        LocalDateTime dt1, LocalDateTime dt2, String sort, Integer from, Integer size,
                                        HitDto hitDto);

    EventDto getById(Long id, HitDto hitDto);

    Event getById(Long id);

    Collection<Event> getByUser(Long id, Integer from, Integer size);

    Event update(Long userId, Event event);

    Event updateAdm(Long id, Event event);

    Event getUserEvent(Long userId, Long eventId);

    Event cancelEvent(Long userId, Long eventId);
}
