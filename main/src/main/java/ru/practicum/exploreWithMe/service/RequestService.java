package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Request;

import java.util.List;

public interface RequestService {

    Request add(long userId, long eventId);

    Request cancel(long userId, long requestId);

    List<Request> getAll(long requesterId, Integer from, Integer size);

    List<Request> getAllOfAuthor(long userId, long eventId, Integer from, Integer size);

    Request confirm(long userId, long eventId, long reqId);

    Request reject(long userId, long eventId, long reqId);
}
