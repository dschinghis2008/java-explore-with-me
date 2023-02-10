package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.exception.ConflictException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.*;
import ru.practicum.exploreWithMe.model.dto.RequestsUpd;
import ru.practicum.exploreWithMe.model.dto.RequestsUpdResult;
import ru.practicum.exploreWithMe.model.mapper.RequestMapper;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.RequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public Request add(long userId, long eventId) {

        Request reqExist = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (reqExist != null) {
            throw new ConflictException(HttpStatus.CONFLICT);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException(HttpStatus.CONFLICT);
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException(HttpStatus.CONFLICT);
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setRequester(user);
        request.setEvent(event);
        if (requestRepository.getCountConfirmed(eventId) >= event.getParticipantLimit()
                && event.getParticipantLimit() > 0) {
            throw new ConflictException(HttpStatus.CONFLICT);
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
        Request request = requestRepository.findByRequesterIdAndId(userId, requestId);
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
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        log.info("---===>>> REQ_SERV get all of Author of request userId=/{}/, event=/{}/", userId, event);
        return requestRepository.findAllByEventId(eventId, pageable);
    }

    @Override
    @Transactional
    public RequestsUpdResult confirm(long userId, long eventId, RequestsUpd upd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        Integer countConfirmed = requestRepository.getCountConfirmed(eventId);
        List<Request> requests = requestRepository.findAllByEventIdAndStatus(eventId, Status.PENDING);
        if (event.getParticipantLimit() <= countConfirmed) {
            requests.forEach(r -> r.setStatus(Status.REJECTED));
            throw new ConflictException(HttpStatus.CONFLICT);
        } else if ((event.getParticipantLimit() - countConfirmed) >= upd.getRequestIds().size()) {
            requests.forEach(r -> r.setStatus(Status.CONFIRMED));
        }
        List<Request> rejected = new ArrayList<>();
        List<Request> confirmed = requestRepository.findAllByIds(upd.getRequestIds());

        confirmed.forEach(request -> request.setStatus(upd.getStatus()));
        log.info("--==>> REQ_SRV confirm count req=/{}/", confirmed.size());
        return requestMapper.toReqUpdRes(confirmed, rejected);
    }

    @Override
    @Transactional
    public RequestsUpdResult reject(long userId, long eventId, RequestsUpd upd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(HttpStatus.CONFLICT);
        }

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = requestRepository.findAllByIds(upd.getRequestIds());

        for (Request request : rejected) {
            if (request.getStatus().equals(Status.CONFIRMED)) {
                throw new ConflictException(HttpStatus.CONFLICT);
            }
            request.setStatus(upd.getStatus());
        }

        log.info("--==>> REQ_SRV reject count req=/{}/", rejected.size());
        return requestMapper.toReqUpdRes(confirmed, rejected);
    }
}
