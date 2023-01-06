package ru.practicum.exploreWithMe.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventAdmController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventDto publish(@PathVariable Long eventId) {
        return eventMapper.toDto(eventService.publish(eventId));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventDto reject(@PathVariable Long eventId) {
        return eventMapper.toDto(eventService.reject(eventId));
    }

    @GetMapping("/admin/events")
    public Collection<EventDto> getEventsAdm(@RequestParam(required = false) Long[] usersId,
                                             @RequestParam(required = false) String[] states,
                                             @RequestParam(required = false) Long[] catId,
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
    public EventAdmDto updateAdm(@PathVariable Long eventId, @RequestBody EventNewDto newDto) {
        return eventMapper.toAdmDto(eventService.updateAdm(eventId, eventMapper.toEventFromNewDto(newDto)));
    }

}
