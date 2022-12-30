package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.model.ViewStats;

import java.util.Collection;

public interface EndpoinService {
    EndpointHit add(EndpointHit endpointHit);
    Collection<ViewStats> getAll(String start, String end, String[] uris, Boolean unique);
}
