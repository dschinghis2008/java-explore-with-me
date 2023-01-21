package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAdmController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventDto publish(@PathVariable long eventId) {
        return eventMapper.toDto(eventService.publish(eventId));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventDto reject(@PathVariable long eventId) {
        return eventMapper.toDto(eventService.reject(eventId));
    }

    @GetMapping("/admin/events")
    public List<EventDto> getEventsAdm(@RequestParam(required = false) Long[] usersId,
                                       @RequestParam(required = false) String[] states,
                                       @RequestParam(required = false) Long[] catId,

                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime rangeStart,

                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,

                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10") @Positive Integer size) {

        List<EventDto> dtos = eventService.getEventsAdm(usersId, states, catId, rangeStart, rangeEnd, from, size);

        log.info("====>>>>EVENT CTRL before eventService: usersId=/{}/, states=/{}/, catId=/{}/, dtStart=/{}/, " +
                        "dtEnd=/{}/, from=/{}/, size=/{}/",
                usersId, states, catId, rangeStart, rangeEnd, from, size);

        return dtos;
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateAdm(@PathVariable long eventId, @RequestBody EventUpdDto updDto) {
        return eventMapper.toFullDto(eventService.updateAdm(eventId, eventMapper.toEventFromUpdDto(updDto)));
    }

}
