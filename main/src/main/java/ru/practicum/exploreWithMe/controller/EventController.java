package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/users/{userId}/events")
    public EventDto add(@PathVariable Integer userId, @RequestBody EventDto dto) {
        log.info("---====>>>>>>EVENT CONTROLLER Before add event, dto=/{}/", dto);
        EventDto newDto = eventMapper.toDto(eventService.add(userId, eventMapper.toEvent(dto)));
        log.info("---====>>>>>>EVENT CONTROLLER after add event newDto=/{}/", newDto);
        return newDto;
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventDto publish(@PathVariable Integer eventId) {
        return eventMapper.toDto(eventService.publish(eventId));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventDto reject(@PathVariable Integer eventId) {
        return eventMapper.toDto(eventService.reject(eventId));
    }

    @GetMapping("/admin/events")
    public Collection<EventDto> getEventsAdm(@RequestParam(required = false) Integer[] usersId,
                                             @RequestParam(required = false) String[] states,
                                             @RequestParam(required = false) Integer[] catId,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @Positive Integer size) {
        LocalDateTime dtStart = null;
        LocalDateTime dtEnd = null;
        if (!(rangeStart == null && rangeEnd == null)) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dtStart = LocalDateTime.parse(rangeStart, f);
            dtEnd = LocalDateTime.parse(rangeEnd, f);
        }
        List<EventDto> list = new ArrayList<>();

        log.info("====>>>>EVENT CTRL before eventService: usersId=/{}/, states=/{}/, catId=/{}/, dtStart=/{}/, dtEnd=/{}/, from=/{}/, size=/{}/",
                usersId, states, catId, dtStart, dtEnd, from, size);

        for (Event event : eventService.getEventsAdm(usersId, states, catId, dtStart, dtEnd, from, size)) {
            list.add(eventMapper.toDto(event));
        }
        return list;
    }

    @PutMapping("/admin/events/{eventId}")
    public EventAdmDto updateAdm(@PathVariable Integer eventId, @RequestBody EventNewDto newDto) {
        return eventMapper.toAdmDto(eventService.updateAdm(eventId, eventMapper.toEventFromNewDto(newDto)));
    }

    @GetMapping("/events")
    public Collection<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                                     @RequestParam(required = false) Integer[] category,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size,
                                                     HttpServletRequest httpServletRequest) {
        LocalDateTime dtStart = null;
        LocalDateTime dtEnd = null;
        if (!(rangeStart == null && rangeEnd == null)) {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dtStart = LocalDateTime.parse(rangeStart, f);
            dtEnd = LocalDateTime.parse(rangeEnd, f);
        }
        List<EventShortDto> list = new ArrayList<>();
        HitDto hitDto = new HitDto();
        hitDto.setApp("EWM");
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        for (Event event : eventService.getEventsPublic(text, category, paid, dtStart, dtEnd, sort, from, size,
                hitDto)) {
            list.add(eventMapper.toShortDto(event));
        }

        if (sort != null) {
            if (sort.equals("VIEWS")) {
                list.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        return list;
    }

    @GetMapping("/events/{id}")
    public EventDto getById(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("EWM");
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return eventMapper.toDto(eventService.getById(id, hitDto));
    }

    @GetMapping("/users/{userId}/events")
    public Collection<EventDto> getByUser(@PathVariable Integer userId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        Collection<EventDto> eventDtos = new ArrayList<>();
        for (Event event : eventService.getByUser(userId, from, size)) {
            eventDtos.add(eventMapper.toDto(event));
        }
        return eventDtos;
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto update(@PathVariable Integer userId, @RequestBody EventNewDto eventDto) {
        log.info("---===>>>EVENT CTRL UPDATE eventDto=/{}/", eventDto);
        Event event = eventMapper.toEventFromNewDto(eventDto);
        log.info("---===>>>EVENT CTRL UPDATE event=/{}/", event);
        return eventMapper.toFullDto(eventService.update(userId, event));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventUserDto getUserEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventMapper.toUserDto(eventService.getUserEvent(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventUserDto cancelEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventMapper.toUserDto(eventService.cancelEvent(userId, eventId));
    }

}
