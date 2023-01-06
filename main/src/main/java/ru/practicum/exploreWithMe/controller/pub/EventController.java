package ru.practicum.exploreWithMe.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
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

        list = eventService.getEventsPublic(text, category, paid, dtStart, dtEnd, sort, from, size, hitDto);
        if (sort != null) {
            if (sort.equals("VIEWS")) {
                list.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        return list;
    }

    @GetMapping("/events/{id}")
    public EventDto getById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("EWM");
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return eventService.getById(id, hitDto);
    }

}
