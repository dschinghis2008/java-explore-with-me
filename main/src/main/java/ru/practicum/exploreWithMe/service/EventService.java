package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.EventDto;
import ru.practicum.exploreWithMe.model.dto.EventShortDto;
import ru.practicum.exploreWithMe.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    Event add(Integer userId, Event event);

    Event publish(Integer eventId);

    Event reject(Integer eventId);

    Collection<Event> getEventsAdm(Integer[] usersId, String[] states, Integer[] catId,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsPublic(String text, Integer[] category, Boolean paid,
                                        LocalDateTime dt1, LocalDateTime dt2, String sort, Integer from, Integer size,
                                        HitDto hitDto);

    EventDto getById(Integer id, HitDto hitDto);

    Event getById(Integer id);

    Collection<Event> getByUser(Integer id, Integer from, Integer size);

    Event update(Integer userId, Event event);

    Event updateAdm(Integer id, Event event);

    Event getUserEvent(Integer userId, Integer eventId);

    Event cancelEvent(Integer userId, Integer eventId);
}
