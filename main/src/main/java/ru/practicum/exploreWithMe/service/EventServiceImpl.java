package ru.practicum.exploreWithMe.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreWithMe.exception.InvalidDataException;
import ru.practicum.exploreWithMe.exception.NotFoundException;
import ru.practicum.exploreWithMe.model.Category;
import ru.practicum.exploreWithMe.model.Event;
import ru.practicum.exploreWithMe.model.State;
import ru.practicum.exploreWithMe.model.User;
import ru.practicum.exploreWithMe.model.dto.*;
import ru.practicum.exploreWithMe.model.mapper.EventMapper;
import ru.practicum.exploreWithMe.repository.CategoryRepository;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.RequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
//@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final EventMapper eventMapper;

    private final RequestRepository requestRepository;

    private final String statUrl;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, EventMapper eventMapper,
                            RequestRepository requestRepository, @Value("${stats-server.url}") String statUrl) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
        this.statUrl = statUrl;
    }

    private Boolean checkDiffTime(LocalDateTime dt1, LocalDateTime dt2, Integer diff) {
        return (dt2.minusHours(diff).isAfter(dt1) || dt2.minusHours(diff).isEqual(dt1));
    }

    @Override
    public Event add(long userId, Event event) {
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        event.setCategory(categoryRepository.findById(event.getCategory().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        if (event.getAnnotation() == null || event.getDescription() == null) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        event.setPublishedOn(LocalDateTime.now().withNano(0));

        log.info("----=====>>>>> added event by user id=/{}/, event=/{}/", userId, event);
        return eventRepository.save(event);
    }

    @Override
    public Event publish(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        event.setPublishedOn(LocalDateTime.now().withNano(0));
        if (event.getState().equals(State.PENDING) &&
                checkDiffTime(event.getPublishedOn(), event.getEventDate(), 1)) {
            event.setState(State.PUBLISHED);
            log.info("-----=====>>> published event id=/{}/", eventId);
            return eventRepository.save(event);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event reject(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
            log.info("-----=====>>> rejected event id=/{}/", eventId);
            return eventRepository.save(event);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<Event> getEventsAdm(Long[] usersId, String[] states, Long[] catId,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (states == null && rangeStart == null && rangeEnd == null) {
            return eventRepository.getAllByInitiatorAndCatg(usersId, catId, pageable).getContent();
        }

        State[] sts = new State[3];
        for (int i = 0; i < Objects.requireNonNull(states).length; i++) {
            sts[i] = State.valueOf(states[i]);
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
                                               LocalDateTime dt2, String sort, Integer from, Integer size,
                                               HitDto hitDto) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("----=====>>>>EVENTSERV Public query events");
        List<Event> events = new ArrayList<>();
        if (text == null && category == null && paid == null && dt1 == null && dt2 == null && sort == null) {
            events = eventRepository.getPublicAll(pageable).getContent();
        }
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
        } else {
            dt1 = LocalDateTime.now();
            dt2 = dt1.plusYears(1000);
            log.info("---===>>>EVENTSERV text=/{}/,categ=/{}/,paid=/{}/,dt1=/{}/,dt2=/{}/,from=/{}/,size=/{}/",
                    text, category, paid, dt1, dt2, from, size);
            events = eventRepository.getEventsPublicAllWithDate(paid, dt1, dt2, pageable).getContent();
        }

        addToStatistic(hitDto);

        List<EventShortDto> result = new ArrayList<>();
        List<EventFullDto> fullDtos = events.stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
        for (EventFullDto e : fullDtos) {
            e.setViews(getViews(e.getId()));
            e.setConfirmedRequests(requestRepository.getCountConfirmed(e.getId()));
            if (e.getConfirmedRequests() <= e.getParticipantLimit()) {
                EventShortDto eventShortDto = eventMapper.toShortFromFull(e);
                eventShortDto.setViews(e.getViews());
                eventShortDto.setConfirmedRequests(e.getConfirmedRequests());
                result.add(eventShortDto);
            }
        }

        return result;
    }

    @Override
    public EventDto getById(long id, HitDto hitDto) {
        addToStatistic(hitDto);
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        return eventMapper.toDto(event);
    }

    @Override
    public Event getById(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Collection<Event> getByUser(long id, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return eventRepository.findAllByInitiatorId(id, pageable).getContent();
    }

    @Override
    public Event update(long userId, Event event) {
        log.info("---===>>> EVENTSERV update: userId=/{}/, event=/{}/", userId, event);
        User user = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findById(event.getCategory().getId()).orElseThrow();
        Event eventUpd = eventRepository.findByIdAndInitiator(event.getId(), user);
        eventUpd.setAnnotation(event.getAnnotation());
        eventUpd.setTitle(event.getTitle());
        eventUpd.setDescription(event.getDescription());
        eventUpd.setCreatedOn(event.getCreatedOn());
        eventUpd.setEventDate(event.getEventDate());
        eventUpd.setLatitude(event.getLatitude());
        eventUpd.setLongitude(event.getLongitude());
        eventUpd.setPaid(event.getPaid());
        eventUpd.setParticipantLimit(event.getParticipantLimit());
        eventUpd.setPublishedOn(event.getPublishedOn());
        eventUpd.setRequestModeration(event.getRequestModeration());
        eventUpd.setState(event.getState());
        eventUpd.setCategory(category);
        eventUpd.setInitiator(user);
        return eventRepository.save(eventUpd);
    }

    @Override
    public Event updateAdm(long id, Event event) {
        log.info("---===>>> EVENTSERV updateAdm: event=/{}/", event);
        Category category = categoryRepository.findById(event.getCategory().getId()).orElseThrow();
        Event eventUpd = eventRepository.findById(id).orElseThrow();
        eventUpd.setAnnotation(event.getAnnotation());
        eventUpd.setTitle(event.getTitle());
        eventUpd.setDescription(event.getDescription());
        eventUpd.setCreatedOn(event.getCreatedOn());
        eventUpd.setEventDate(event.getEventDate());
        eventUpd.setLatitude(event.getLatitude());
        eventUpd.setLongitude(event.getLongitude());
        eventUpd.setPaid(event.getPaid());
        eventUpd.setParticipantLimit(event.getParticipantLimit());
        eventUpd.setPublishedOn(event.getPublishedOn());
        eventUpd.setRequestModeration(event.getRequestModeration());
        eventUpd.setState(event.getState());
        eventUpd.setCategory(category);
        eventUpd.setInitiator(event.getInitiator());
        return eventRepository.save(eventUpd);
    }

    @Override
    public Event getUserEvent(long userId, long eventId) {
        log.info("---===>>> EVENTSERV query current user's event: userId=/{}/, event=/{}/", userId, eventId);
        return eventRepository.findByIdAndInitiatorId(eventId, userId);
    }

    @Override
    public Event cancelEvent(long userId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        event.setState(State.CANCELED);
        log.info("---===>>> EVENTSERV cancel current user's event: userId=/{}/, event=/{}/", userId, event);
        return eventRepository.save(event);
    }

    private void addToStatistic(HitDto hitDto) {
        String url = statUrl + "/hit";
        log.info("---===>>>EVENTSERV hitDto=/{}/, url=/{}/", hitDto, url);
        HttpEntity<HitDto> request = new HttpEntity<>(hitDto);
        restTemplate.postForObject(url, request, HitDto.class);
    }

    private Integer getViews(Long eventId) {
        ViewStatsDto[] views = restTemplate.getForObject(statUrl + "/stats?uris=/events/"
                + eventId.toString(), ViewStatsDto[].class);
        if (views != null) {
            if (views.length > 0) {
                return views[0].getHits().intValue();
            }
        }
        return null;

    }

}
