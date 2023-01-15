package ru.practicum.exploreWithMe.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.service.EventService;
import ru.practicum.exploreWithMe.service.webClient.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@Validated
public class EventController {
    private final EventService eventService;
    private final WebClient webClient;

    private final String appName;

    public EventController(EventService eventService, WebClient webClient, @Value("${APP}") String appName) {
        this.eventService = eventService;
        this.webClient = webClient;
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
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeEnd,

                                               @RequestParam(required = false) String sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size,
                                               HttpServletRequest httpServletRequest) {

        List<EventShortDto> shortDtos = eventService.getEventsPublic(text, category, paid, rangeStart, rangeEnd,
                sort, from, size);
        log.info("--==>>EVENT_CTRL query ALL PUBLIC count = /{}/", shortDtos.size());
        webClient.addToStatistic(httpServletRequest, appName);
        ViewStatsDto[] viewStatsDtos = webClient.getViews(shortDtos.toArray(new EventShortDto[0]));
        for (ViewStatsDto view : viewStatsDtos) {
            log.info("--==>>substr uri = /{}/",
                    view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            Long viewId = Long.parseLong(view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            shortDtos.stream()
                    .filter(eventShortDto -> Objects.equals(eventShortDto.getId(), viewId))
                    .forEach(eventShortDto -> eventShortDto.setViews(Math.toIntExact(view.getHits())));
        }
        if (sort != null) {
            if (sort.equals("VIEWS")) {
                shortDtos.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        return shortDtos;
    }

    @GetMapping("/events/{id}")
    public EventDto getById(@PathVariable long id, HttpServletRequest httpServletRequest) {
        EventDto eventDto = eventService.getById(id);
        eventDto.setViews(webClient.findView(id));
        webClient.addToStatistic(httpServletRequest, appName);
        return eventDto;
    }

}
