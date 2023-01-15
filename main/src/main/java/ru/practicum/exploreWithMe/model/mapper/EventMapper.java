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

    public Event toEvent(EventDto eventDto) {
        Category category = new Category();
        category.setId(eventDto.getCategory());
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setAnnotation(eventDto.getAnnotation());
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setCreatedOn(eventDto.getCreatedOn());
        event.setEventDate(eventDto.getEventDate());
        event.setLatitude(eventDto.getLocation().getLat());
        event.setLongitude(eventDto.getLocation().getLon());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setPublishedOn(eventDto.getPublishedOn());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setState(eventDto.getState());
        event.setCategory(category);
        event.setInitiator(userMapper.toUser(eventDto.getInitiator()));
        return event;
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

    public Event toEventFromNewDto(EventUpdateDto eventNewDto) {
        Event event = new Event();

        Category category = new Category();
        category.setId(eventNewDto.getCategory());
        event.setId(eventNewDto.getEventId());
        event.setAnnotation(eventNewDto.getAnnotation());
        event.setTitle(eventNewDto.getTitle());
        event.setDescription(eventNewDto.getDescription());
        event.setCreatedOn(eventNewDto.getCreatedOn());
        event.setEventDate(eventNewDto.getEventDate());
        event.setPaid(eventNewDto.getPaid());
        event.setParticipantLimit(eventNewDto.getParticipantLimit());
        event.setPublishedOn(eventNewDto.getPublishedOn());
        if (eventNewDto.getRequestModeration() == null) {
            eventNewDto.setRequestModeration(false);
        }
        event.setRequestModeration(eventNewDto.getRequestModeration());
        event.setState(eventNewDto.getState());
        event.setCategory(category);
        event.setInitiator(userMapper.toUser(eventNewDto.getInitiator()));
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

    public List<EventDto> toDtos(List<Event> events){
        return events.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
