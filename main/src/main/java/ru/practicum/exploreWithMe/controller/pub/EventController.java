package ru.practicum.exploreWithMe.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@Slf4j
@Validated
public class EventController {
    private final EventService eventService;
    private final String appName;

    public EventController(EventService eventService,  @Value("${APP}") String appName) {
        this.eventService = eventService;
        this.appName = appName;
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Integer[] category,
                                               @RequestParam(required = false) Boolean paid,

                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeStart,

                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,

                                               @RequestParam(required = false) String sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size,
                                               HttpServletRequest httpServletRequest) {

        List<EventShortDto> list = new ArrayList<>();
        HitDto hitDto = new HitDto();
        hitDto.setApp(appName);
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        list = eventService.getEventsPublic(text, category, paid, rangeStart, rangeEnd, sort, from, size, hitDto);
        if (sort != null) {
            if (sort.equals("VIEWS")) {
                list.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        return list;
    }

    @GetMapping("/events/{id}")
    public EventDto getById(@PathVariable long id, HttpServletRequest httpServletRequest) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(appName);
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return eventService.getById(id, hitDto);
    }

}
