package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventPrivateController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/users/{userId}/events")
    public EventDto add(@PathVariable long userId, @RequestBody @Valid EventDto dto) {
        log.info("---====>>>>>>EVENT CONTROLLER Before add event, dto=/{}/", dto);
        EventDto newDto = eventMapper.toDto(eventService.add(userId, eventMapper.toEvent(dto)));
        log.info("---====>>>>>>EVENT CONTROLLER after add event newDto=/{}/", newDto);
        return newDto;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> getByUser(@PathVariable long userId,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<Event> events = eventService.getByUser(userId, from, size);
        return eventMapper.toDtos(events);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto update(@PathVariable long userId, @RequestBody @Valid EventUpdateDto eventDto) {
        log.info("---===>>>EVENT CTRL UPDATE eventDto=/{}/", eventDto);
        Event event = eventMapper.toEventFromNewDto(eventDto);
        log.info("---===>>>EVENT CTRL UPDATE event=/{}/", event);
        return eventMapper.toFullDto(eventService.update(userId, event));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventUserDto getUserEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("---===>>>EVENT CTRL query eventId=/{}/", eventId);
        return eventMapper.toUserDto(eventService.getUserEvent(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventUserDto cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("---===>>>EVENT CTRL cacel eventId=/{}/", eventId);
        return eventMapper.toUserDto(eventService.cancelEvent(userId, eventId));
    }

}
