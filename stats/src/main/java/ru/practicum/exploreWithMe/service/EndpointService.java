package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.model.ViewStats;

import java.util.List;

public interface EndpointService {
    EndpointHit add(EndpointHit endpointHit);

    List<ViewStats> getAll(String start, String end, String[] uris, Boolean unique);

}
