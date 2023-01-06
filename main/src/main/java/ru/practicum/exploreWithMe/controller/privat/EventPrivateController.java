package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/users/{userId}/events")
    public EventDto add(@PathVariable Long userId, @RequestBody EventDto dto) {
        log.info("---====>>>>>>EVENT CONTROLLER Before add event, dto=/{}/", dto);
        EventDto newDto = eventMapper.toDto(eventService.add(userId, eventMapper.toEvent(dto)));
        log.info("---====>>>>>>EVENT CONTROLLER after add event newDto=/{}/", newDto);
        return newDto;
    }

    @GetMapping("/users/{userId}/events")
    public Collection<EventDto> getByUser(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        Collection<EventDto> eventDtos = new ArrayList<>();
        for (Event event : eventService.getByUser(userId, from, size)) {
            eventDtos.add(eventMapper.toDto(event));
        }
        return eventDtos;
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto update(@PathVariable Long userId, @RequestBody EventNewDto eventDto) {
        log.info("---===>>>EVENT CTRL UPDATE eventDto=/{}/", eventDto);
        Event event = eventMapper.toEventFromNewDto(eventDto);
        log.info("---===>>>EVENT CTRL UPDATE event=/{}/", event);
        return eventMapper.toFullDto(eventService.update(userId, event));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventUserDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventMapper.toUserDto(eventService.getUserEvent(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventUserDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventMapper.toUserDto(eventService.cancelEvent(userId, eventId));
    }

}
