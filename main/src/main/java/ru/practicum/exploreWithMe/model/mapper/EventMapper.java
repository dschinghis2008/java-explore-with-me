package ru.practicum.exploreWithMe.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventDto toDto(Event event) {
        EventDto eventDto = new EventDto();

        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setTitle(event.getTitle());
        eventDto.setDescription(event.getDescription());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setEventDate(event.getEventDate());

        LocationDto locationDto = new LocationDto();
        locationDto.setLat(event.getLatitude());
        locationDto.setLon(event.getLongitude());
        eventDto.setLocation(locationDto);

        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setCategory(event.getCategory().getId());
        eventDto.setInitiator(userMapper.toDto(event.getInitiator()));
        return eventDto;
    }

    public EventShortDto toShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setCategory(event.getCategory().getId());
        eventShortDto.setInitiator(userMapper.toDto(event.getInitiator()));

        return eventShortDto;
    }

    public EventShortDto toShortFromFull(EventFullDto eventFullDto) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(eventFullDto.getId());
        eventShortDto.setAnnotation(eventFullDto.getAnnotation());
        eventShortDto.setTitle(eventFullDto.getTitle());
        eventShortDto.setEventDate(eventFullDto.getEventDate());
        eventShortDto.setPaid(eventFullDto.getPaid());
        eventShortDto.setCategory(eventFullDto.getCategory().getId());
        eventShortDto.setInitiator(eventFullDto.getInitiator());
        eventShortDto.setConfirmedRequests(eventFullDto.getConfirmedRequests());
        eventShortDto.setViews(eventFullDto.getViews());

        return eventShortDto;
    }

    public EventUserDto toUserDto(Event event) {
        EventUserDto eventDto = new EventUserDto();

        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setTitle(event.getTitle());
        eventDto.setDescription(event.getDescription());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setEventDate(event.getEventDate());

        LocationDto locationDto = new LocationDto();
        locationDto.setLat(event.getLatitude());
        locationDto.setLon(event.getLongitude());
        eventDto.setLocation(locationDto);

        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setCategory(event.getCategory().getId());
        eventDto.setInitiator(userMapper.toDto(event.getInitiator()));
        return eventDto;
    }

    public Event toEventFromNewDto(EventNewDto inDto) {
        Event event = new Event();

        Category category = new Category();
        category.setId(inDto.getCategory());
        event.setAnnotation(inDto.getAnnotation());
        event.setTitle(inDto.getTitle());
        event.setDescription(inDto.getDescription());
        event.setEventDate(inDto.getEventDate());
        event.setPaid(inDto.getPaid());
        event.setParticipantLimit(inDto.getParticipantLimit());
        if (inDto.getRequestModeration() == null) {
            inDto.setRequestModeration(false);
        }

        if (inDto.getLocation() != null) {
            event.setLatitude(inDto.getLocation().getLat());
            event.setLongitude(inDto.getLocation().getLon());
        }

        event.setRequestModeration(inDto.getRequestModeration());
        event.setCategory(category);
        return event;
    }

    public Event toEventFromAdmDto(EventAdmDto inDto) {
        Event event = new Event();
        Category category = new Category();
        category.setId(inDto.getCategory());
        event.setAnnotation(inDto.getAnnotation());
        event.setTitle(inDto.getTitle());
        event.setDescription(inDto.getDescription());
        event.setEventDate(inDto.getEventDate());
        event.setPaid(inDto.getPaid());
        event.setParticipantLimit(inDto.getParticipantLimit());
        if (inDto.getLocation() != null) {
            event.setLatitude(inDto.getLocation().getLat());
            event.setLongitude(inDto.getLocation().getLon());
        }
        event.setRequestModeration(inDto.getRequestModeration());
        event.setCategory(category);
        return event;
    }

    public Event toEventFromUpdDto(EventUpdDto inDto) {
        Event event = new Event();
        Category category = new Category();
        category.setId(inDto.getCategory());
        event.setId(inDto.getEventId());
        event.setAnnotation(inDto.getAnnotation());
        event.setTitle(inDto.getTitle());
        event.setDescription(inDto.getDescription());
        event.setEventDate(inDto.getEventDate());
        event.setPaid(inDto.getPaid());
        event.setParticipantLimit(inDto.getParticipantLimit());
        if (inDto.getRequestModeration() == null) {
            inDto.setRequestModeration(false);
        }

        if (inDto.getLocation() != null) {
            event.setLatitude(inDto.getLocation().getLat());
            event.setLongitude(inDto.getLocation().getLon());
        }

        event.setRequestModeration(inDto.getRequestModeration());
        event.setCategory(category);
        return event;
    }

    public EventFullDto toFullDto(Event event) {
        EventFullDto eventDto = new EventFullDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setTitle(event.getTitle());
        eventDto.setDescription(event.getDescription());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setEventDate(event.getEventDate());

        LocationDto locationDto = new LocationDto();
        locationDto.setLat(event.getLatitude());
        locationDto.setLon(event.getLongitude());
        eventDto.setLocation(locationDto);

        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setCategory(categoryMapper.toDto(event.getCategory()));
        eventDto.setInitiator(userMapper.toDto(event.getInitiator()));
        return eventDto;
    }

    public List<EventDto> toDtos(List<Event> events) {
        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
