package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.dto.RequestsUpd;
import ru.practicum.exploreWithMe.model.dto.RequestsUpdResult;

import java.util.List;

public interface RequestService {

    Request add(long userId, long eventId);

    Request cancel(long userId, long requestId);

    List<Request> getAll(long requesterId, Integer from, Integer size);

    List<Request> getAllOfAuthor(long userId, long eventId, Integer from, Integer size);

    RequestsUpdResult confirm(long userId, long eventId, RequestsUpd upd);

    RequestsUpdResult reject(long userId, long eventId, RequestsUpd upd);
}
