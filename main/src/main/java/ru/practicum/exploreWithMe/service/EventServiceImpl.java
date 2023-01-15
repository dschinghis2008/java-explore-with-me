package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.*;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.repository.CategoryRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.RequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    private final RequestRepository requestRepository;

    private Boolean checkDiffTime(LocalDateTime dt1, LocalDateTime dt2, Integer diff) {
        return (dt2.minusHours(diff).isAfter(dt1) || dt2.minusHours(diff).isEqual(dt1));
    }

    @Override
    @Transactional
    public Event add(long userId, Event event) {
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        event.setCategory(categoryRepository.findById(event.getCategory().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        if (event.getAnnotation() == null || event.getDescription() == null) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        event.setPublishedOn(LocalDateTime.now().withNano(0));

        log.info("----=====>>>>> added event by user id=/{}/, event=/{}/", userId, event);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event publish(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        event.setPublishedOn(LocalDateTime.now().withNano(0));
        if (event.getState().equals(EventState.PENDING) &&
                checkDiffTime(event.getPublishedOn(), event.getEventDate(), 1)) {
            event.setState(EventState.PUBLISHED);
            log.info("-----=====>>> published event id=/{}/", eventId);
            return event;
        } else {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public Event reject(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
            log.info("-----=====>>> rejected event id=/{}/", eventId);
            return event;
        } else {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Event> getEventsAdm(Long[] usersId, String[] states, Long[] catId,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (states == null && rangeStart == null && rangeEnd == null) {
            return eventRepository.getAllByInitiatorAndCatg(usersId, catId, pageable).getContent();
        }

        EventState[] sts = new EventState[3];
        for (int i = 0; i < Objects.requireNonNull(states).length; i++) {
            sts[i] = EventState.valueOf(states[i]);
        }

        log.info("----=====>>>>>EVENT_SERV admin-query events");
        if (usersId == null && catId == null) {
            return eventRepository.getEventsAdmAll(sts, rangeStart, rangeEnd, pageable).getContent();
        } else {
            return eventRepository.getEventsAdm(usersId, sts, catId, rangeStart, rangeEnd, pageable).getContent();
        }

    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, Integer[] category, Boolean paid, LocalDateTime dt1,
                                               LocalDateTime dt2, String sort, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("----=====>>>>EVENTSERV Public query events");
        List<Event> events = new ArrayList<>();

        if (dt1 == null && dt2 == null && sort == null && paid != null) {
            events = eventRepository.getEventsPublicByDescrAndPaid(text, paid, pageable).getContent();
        }
        if (category == null && dt1 != null && dt2 != null && sort.equals("EVENT_DATE")) {
            log.info("---===>>>EVENTSERV text=/{}/,categ=/{}/,paid=/{}/,dt1=/{}/,dt2=/{}/,from=/{}/,size=/{}/",
                    text, category, paid, dt1, dt2, from, size);
            events = eventRepository.getEventsPublicAllCategSortByDate(text, paid,
                    dt1, dt2, pageable).getContent();
        } else if (category == null && Objects.equals(text, "0") && dt1 != null && dt2 != null) {
            log.info("---===>>>EVENTSERV text=/{}/,categ=/{}/,paid=/{}/,dt1=/{}/,dt2=/{}/,from=/{}/,size=/{}/",
                    text, category, paid, dt1, dt2, from, size);
            events = eventRepository.getEventsPublicAllWithDate(paid, dt1, dt2, pageable).getContent();
        } else if (text == null && category == null && paid == null && dt1 == null && dt2 == null && sort == null) {
            events = eventRepository.getPublicAll(pageable).getContent();
            log.info("--==>>EVENTSERV Public query events getPublicAll");
        } else {
            dt1 = LocalDateTime.now();
            dt2 = dt1.plusYears(1000);
            log.info("---===>>>EVENTSERV text=/{}/,categ=/{}/,paid=/{}/,dt1=/{}/,dt2=/{}/,from=/{}/,size=/{}/",
                    text, category, paid, dt1, dt2, from, size);
            events = eventRepository.getEventsPublicAllWithDate(paid, dt1, dt2, pageable).getContent();
        }

        return events.stream()
                .map(eventMapper::toShortDto)
                .peek(e -> e.setConfirmedRequests(requestRepository.getCountConfirmed(e.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getById(long id) {
        EventDto dto = eventMapper.toDto(eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        dto.setConfirmedRequests(requestRepository.getCountConfirmed(id));
        log.info("--==>>EVENTSERV public getById id=/{}/", id);
        return dto;
    }

    @Override
    public List<Event> getByUser(long id, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("--==>>EVENTSERV query events by user id=/{}/", id);
        return eventRepository.findAllByInitiatorId(id, pageable).getContent();
    }

    @Override
    @Transactional
    public Event update(long userId, Event event) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Category category = categoryRepository.findById(event.getCategory().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event eventUpd = eventRepository.findByIdAndInitiator(event.getId(), user);
        if (eventUpd == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        if (event.getAnnotation() != null) {
            eventUpd.setAnnotation(event.getAnnotation());
        }
        if (event.getTitle() != null) {
            eventUpd.setTitle(event.getTitle());
        }
        if (event.getDescription() != null) {
            eventUpd.setDescription(event.getDescription());
        }
        if (event.getCreatedOn() != null) {
            eventUpd.setCreatedOn(event.getCreatedOn());
        }
        if (event.getEventDate() != null) {
            eventUpd.setEventDate(event.getEventDate());
        }
        eventUpd.setLatitude(event.getLatitude());
        eventUpd.setLongitude(event.getLongitude());
        eventUpd.setPaid(event.getPaid());
        eventUpd.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventUpd.setPublishedOn(event.getPublishedOn());
        }
        eventUpd.setRequestModeration(event.getRequestModeration());
        if (event.getState() != null) {
            eventUpd.setState(event.getState());
        }
        if (event.getCategory() != null) {
            eventUpd.setCategory(category);
        }
        if (event.getInitiator() != null) {
            eventUpd.setInitiator(user);
        }
        log.info("---===>>> EVENTSERV update: userId=/{}/, event=/{}/", userId, event);
        return eventUpd;
    }

    @Override
    @Transactional
    public Event updateAdm(long id, Event event) {
        log.info("---===>>> EVENTSERV updateAdm: event=/{}/", event);
        Category category = categoryRepository.findById(event.getCategory().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Event eventUpd = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (eventUpd == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        if (event.getAnnotation() != null) {
            eventUpd.setAnnotation(event.getAnnotation());
        }
        if (event.getTitle() != null) {
            eventUpd.setTitle(event.getTitle());
        }
        if (event.getDescription() != null) {
            eventUpd.setDescription(event.getDescription());
        }
        if (event.getCreatedOn() != null) {
            eventUpd.setCreatedOn(event.getCreatedOn());
        }
        if (event.getEventDate() != null) {
            eventUpd.setEventDate(event.getEventDate());
        }
        eventUpd.setLatitude(event.getLatitude());
        eventUpd.setLongitude(event.getLongitude());
        eventUpd.setPaid(event.getPaid());
        eventUpd.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventUpd.setPublishedOn(event.getPublishedOn());
        }
        eventUpd.setRequestModeration(event.getRequestModeration());
        if (event.getState() != null) {
            eventUpd.setState(event.getState());
        }
        if (event.getCategory() != null) {
            eventUpd.setCategory(category);
        }
        if (event.getInitiator() != null) {
            eventUpd.setInitiator(event.getInitiator());
        }
        return eventUpd;
    }

    @Override
    public Event getUserEvent(long userId, long eventId) {
        log.info("---===>>> EVENTSERV query current user's event: userId=/{}/, event=/{}/", userId, eventId);
        return eventRepository.findByIdAndInitiatorId(eventId, userId);
    }

    @Override
    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        event.setState(EventState.CANCELED);
        log.info("---===>>> EVENTSERV cancel current user's event: userId=/{}/, event=/{}/", userId, event);
        return event;
    }

}
