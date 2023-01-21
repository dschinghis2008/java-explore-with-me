package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.EventDto;
import ru.practicum.exploreWithMe.model.dto.EventFullDto;
import ru.practicum.exploreWithMe.model.dto.EventShortDto;
import ru.practicum.exploreWithMe.model.dto.EventUserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event add(long userId, Event event);

    Event publish(long eventId);

    Event reject(long eventId);

    List<EventDto> getEventsAdm(Long[] usersId, String[] states, Long[] catId,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsPublic(String text, Integer[] category, Boolean paid, LocalDateTime dt1,
                                        LocalDateTime dt2, Boolean onlyAvailable, String sort,
                                        Integer from, Integer size, HttpServletRequest httpServletRequest);

    EventDto getById(long id, HttpServletRequest httpServletRequest);

    List<EventDto> getByUser(long id, Integer from, Integer size);

    Event update(long userId, Event event);

    Event updateAdm(long id, Event event);

    EventUserDto getUserEvent(long userId, long eventId);

    Event cancelEvent(long userId, long eventId);
}
