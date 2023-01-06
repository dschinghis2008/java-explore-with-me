package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Request;

import java.util.Collection;

public interface RequestService {

    Request add(Long userId, Long eventId);

    Request cancel(Long userId, Long requestId);

    Collection<Request> getAll(Long requesterId);

    Collection<Request> getAllOfAuthor(Long userId, Long eventId);

    Request confirm(Long userId, Long eventId, Long reqId);

    Request reject(Long userId, Long eventId, Long reqId);
}
