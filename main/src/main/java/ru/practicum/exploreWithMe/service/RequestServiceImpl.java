package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.Status;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.RequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Request add(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException(HttpStatus.CONFLICT);
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setRequester(user);
        request.setEvent(event);
        if (!event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        log.info("---===>>>REQ_SERV added request=/{}/", request);
        return requestRepository.save(request);
    }

    @Override
    public Request cancel(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        request.setStatus(Status.CANCELED);
        log.info("---===>>>REQ_SERV canceled request /{}/", request);
        return requestRepository.save(request);
    }

    @Override
    public Collection<Request> getAll(long requesterId) {
        log.info("---===>>>REQ_SERV query all requests userId= /{}/", requesterId);
        return requestRepository.findAllByRequesterOrderByCreated(requesterId);
    }

    @Override
    public Collection<Request> getAllOfAuthor(long userId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        log.info("---===>>> REQ_SERV get all of Author of request userId=/{}/, event=/{}/", userId, event);
        return requestRepository.findAllByEventId(eventId);
    }

    @Override
    public Request confirm(long userId, long eventId, long reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!request.getEvent().getId().equals(eventId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        if (event.getParticipantLimit().equals(requestRepository.getCountConfirmed(eventId) - 1)) {
            for (Request r : requestRepository.findAllByEventIdAndStatus(eventId, Status.PENDING)) {
                r.setStatus(Status.REJECTED);
                requestRepository.save(r);
            }
            request.setStatus(Status.CONFIRMED);
        } else if (event.getParticipantLimit() < requestRepository.getCountConfirmed(eventId)) {
            request.setStatus(Status.REJECTED);
        } else {
            request.setStatus(Status.CONFIRMED);
        }
        log.info("---===>>> REQ_SERV query confirmed request = /{}/", request);
        return requestRepository.save(request);
    }

    @Override
    public Request reject(long userId, long eventId, long reqId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!request.getEvent().getId().equals(eventId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        request.setStatus(Status.REJECTED);
        return requestRepository.save(request);
    }
}
