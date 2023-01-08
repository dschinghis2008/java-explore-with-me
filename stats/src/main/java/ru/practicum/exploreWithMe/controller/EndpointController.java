package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.EndpointHitDto;
import ru.practicum.exploreWithMe.model.Mapper;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.service.EndpointServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EndpointController {
    private final EndpointServiceImpl endpointService;
    private final Mapper mapper;

    @PostMapping("/hit")
    public EndpointHitDto add(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("-==>>ENDPNTCTRL dto=/{}/", endpointHitDto);
        return mapper.toDto(endpointService.add(mapper.toEndpoitHit(endpointHitDto)));
    }

    @GetMapping("/stats")
    public List<ViewStats> getAll(@RequestParam(required = false) String start,
                                        @RequestParam(required = false) String end,
                                        @RequestParam String[] uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("-==>>ENDPNTCTRL query stats start=/{}/,end=/{}/,uris=/{}/,uniq=/{}/", start, end, uris, unique);
        return endpointService.getAll(start, end, uris, unique);
    }
}
