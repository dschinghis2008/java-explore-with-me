package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.*;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.RequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Request add(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException(HttpStatus.CONFLICT);
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setRequester(user);
        request.setEvent(event);
        if (requestRepository.getCountConfirmed(eventId) >= event.getParticipantLimit()) {
            throw new NotFoundException(HttpStatus.CONFLICT);
        }
        if (!event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        log.info("---===>>>REQ_SERV added request=/{}/", request);
        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public Request cancel(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        request.setStatus(Status.CANCELED);
        log.info("---===>>>REQ_SERV canceled request /{}/", request);
        return request;
    }

    @Override
    public List<Request> getAll(long requesterId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("---===>>>REQ_SERV query all requests userId= /{}/", requesterId);
        return requestRepository.findAllByRequesterOrderByCreated(requesterId, pageable);
    }

    @Override
    public List<Request> getAllOfAuthor(long userId, long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        log.info("---===>>> REQ_SERV get all of Author of request userId=/{}/, event=/{}/", userId, event);
        return requestRepository.findAllByEventId(eventId, pageable);
    }

    @Override
    @Transactional
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
        if (event.getParticipantLimit() == (requestRepository.getCountConfirmed(eventId) + 1)) {
            List<Request> requests = requestRepository.findAllByEventIdAndStatus(eventId, Status.PENDING);
            requests.forEach(r -> r.setStatus(Status.REJECTED));
            request.setStatus(Status.CONFIRMED);
        } else if (event.getParticipantLimit() < requestRepository.getCountConfirmed(eventId)) {
            request.setStatus(Status.REJECTED);
        } else {
            request.setStatus(Status.CONFIRMED);
        }
        log.info("---===>>> REQ_SERV query confirmed request = /{}/", request);
        return request;
    }

    @Override
    @Transactional
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
        return request;
    }
}
