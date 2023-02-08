package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.StateAction;
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
    public ResponseEntity<EventDto> add(@PathVariable long userId, @RequestBody @Valid EventNewDto dto) {
        log.info("---====>>>>>>EVENT CONTROLLER Before add event, dto=/{}/", dto);
        EventDto newDto = eventMapper.toDto(eventService.add(userId, eventMapper.toEventFromNewDto(dto)));
        log.info("---====>>>>>>EVENT CONTROLLER after add event newDto=/{}/", newDto);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> getByUser(@PathVariable long userId,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<EventDto> dtos = eventService.getByUser(userId, from, size);
        return dtos;
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto update(@PathVariable long userId, @PathVariable long eventId,
                               @RequestBody @Valid EventUpdDto eventDto) {
        log.info("---===>>>EVENT CTRL UPDATE eventDto=/{}/", eventDto);
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            EventDto dto = eventMapper.toDto(eventService.cancelEvent(userId, eventId));
            log.info("---===>>>EVENT CTRL UPDATE categ=/{}/", dto.getCategory());
            return dto;
        } else {
            Event event = eventMapper.toEventFromUpdDto(eventDto);
            log.info("---===>>>EVENT CTRL UPDATE event=/{}/", event.getEventDate());
            return eventMapper.toDto(eventService.update(userId, eventId, event));
        }
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventUserDto getUserEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("---===>>>EVENT CTRL query eventId=/{}/", eventId);
        return eventService.getUserEvent(userId, eventId);
    }

}
