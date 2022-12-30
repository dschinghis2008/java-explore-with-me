package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Request;

import java.util.Collection;

public interface RequestService {

    Request add(Integer userId, Integer eventId);
    Request cancel(Integer userId, Integer requestId);
    Collection<Request> getAll(Integer requesterId);

    Collection<Request> getAllOfAuthor(Integer userId, Integer eventId);

    Request confirm(Integer userId, Integer eventId, Integer reqId);

    Request reject(Integer userId, Integer eventId, Integer reqId);
}
