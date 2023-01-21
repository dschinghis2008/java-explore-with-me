package ru.practicum.exploreWithMe.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
import ru.practicum.exploreWithMe.service.webClient.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
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

    private final WebClient webClient;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private Boolean checkDiffTime(LocalDateTime dt1, LocalDateTime dt2, Integer diff) {
        return (dt2.minusHours(diff).isAfter(dt1) || dt2.minusHours(diff).isEqual(dt1));
    }

    @Override
    @Transactional
    public Event add(long userId, Event event) {
        if (!checkDiffTime(LocalDateTime.now(), event.getEventDate(), 2)) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST);
        }
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        event.setCategory(categoryRepository.findById(event.getCategory().getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));

        log.info("----=====>>>>> added event by user id=/{}/, event=/{}/", userId, event);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event publish(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (event.getState().equals(EventState.PENDING) &&
                checkDiffTime(LocalDateTime.now().withNano(0), event.getEventDate(), 1)) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now().withNano(0));
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
    public List<EventDto> getEventsAdm(Long[] usersId, String[] states, Long[] catId,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                       Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = new ArrayList<>();
        QEvent qEvent = QEvent.event;
        BooleanExpression predicate = qEvent.isNotNull();

        if (!(usersId == null || usersId[0] == 0)) {
            predicate = predicate.and(qEvent.initiator.id.in(usersId));
        }

        if (states != null) {
            EventState[] sts = new EventState[3];
            for (int i = 0; i < Objects.requireNonNull(states).length; i++) {
                sts[i] = EventState.valueOf(states[i]);
            }
            predicate = predicate.and(qEvent.state.in(sts));
        }

        if (!(catId == null || catId[0] == 0)) {
            predicate = predicate.and(qEvent.category.id.in(catId));
        }

        if (rangeStart != null && rangeEnd != null) {
            predicate = predicate.and(qEvent.eventDate.between(rangeStart, rangeEnd));
        }

        log.info("--===>>>EVENTSERV ADMIN query events predicate= /{}/", predicate.toString());
        events = eventRepository.findAll(predicate, pageable).getContent();
        log.info("-=>>EV_SERV_ADM events=/{}/", events.toString());

        List<EventsCountConfirmed> countConfirm = getConfirmed(events);
        log.info("-=>>EV_SERV countConfirm=/{}/", countConfirm.size());

        List<EventDto> dtos = events.stream().map(eventMapper::toDto).collect(Collectors.toList());
        ViewStatsDto[] viewStatsDtos = webClient.getViews(dtos.toArray(new EventDto[0]));
        for (ViewStatsDto view : viewStatsDtos) {
            log.info("--==>>substr uri = /{}/",
                    view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            Long viewId = Long.parseLong(view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            dtos.stream()
                    .filter(eventFullDto -> Objects.equals(eventFullDto.getId(), viewId))
                    .forEach(eventFullDto -> eventFullDto.setViews(Math.toIntExact(view.getHits())));
        }

        for (EventsCountConfirmed eventsCount : countConfirm) {
            dtos.stream().filter(eventDto -> eventDto.getId().equals(eventsCount.getId()))
                    .forEach(eventDto -> eventDto.setConfirmedRequests(eventsCount.getCount()));
        }

        return dtos;
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, Integer[] category, Boolean paid, LocalDateTime dt1,
                                               LocalDateTime dt2, Boolean onlyAvailable, String sort,
                                               Integer from, Integer size, HttpServletRequest httpServletRequest) {
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = new ArrayList<>();

        QEvent qEvent = QEvent.event;
        BooleanExpression predicate = qEvent.isNotNull();
        if (!(text == null || text.isBlank() || text.equals("0"))) {
            predicate = predicate.and(qEvent.annotation.toUpperCase().contains(text.toUpperCase())
                    .or(qEvent.description.toUpperCase().contains(text.toUpperCase())));
        }

        if (!(category == null || category[0] == 0)) {
            predicate = predicate.and(qEvent.category.id.in(category));
        }

        if (paid != null) {
            predicate = predicate.and(qEvent.paid.eq(paid));
        }

        if (dt1 != null && dt2 != null) {
            predicate = predicate.and(qEvent.eventDate.between(dt1, dt2));
        }

        log.info("----=====>>>>EVENTSERV Public query events predicate= /{}/", predicate.toString());
        events = eventRepository.findAll(predicate, pageable).getContent();
        log.info("-=>>EV_SERV events=/{}/", events.get(0).toString());

        List<EventsCountConfirmed> countConfirm = getConfirmed(events);
        log.info("-=>>EV_SERV countConfirm=/{}/", countConfirm.size());

        List<EventFullDto> fullDtos = events.stream().map(eventMapper::toFullDto).collect(Collectors.toList());
        webClient.addToStatistic(httpServletRequest);
        ViewStatsDto[] viewStatsDtos = webClient.getFullViews(fullDtos.toArray(new EventFullDto[0]));
        for (ViewStatsDto view : viewStatsDtos) {
            log.info("--==>>substr uri = /{}/",
                    view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            Long viewId = Long.parseLong(view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            fullDtos.stream()
                    .filter(eventShortDto -> Objects.equals(eventShortDto.getId(), viewId))
                    .forEach(eventShortDto -> eventShortDto.setViews(Math.toIntExact(view.getHits())));
        }

        for (EventsCountConfirmed eventsCount : countConfirm) {
            fullDtos.stream().filter(eventFullDto -> eventFullDto.getId().equals(eventsCount.getId()))
                    .forEach(eventFullDto -> eventFullDto.setConfirmedRequests(eventsCount.getCount()));
        }
        List<EventShortDto> dtos = new ArrayList<>();
        if (onlyAvailable != null) {
            if (onlyAvailable) {
                dtos = fullDtos.stream()
                        .filter(e -> e.getConfirmedRequests() <= e.getParticipantLimit())
                        .map(eventMapper::toShortFromFull)
                        .collect(Collectors.toList());
            } else {
                dtos = fullDtos.stream()
                        .map(eventMapper::toShortFromFull)
                        .collect(Collectors.toList());
            }
        }

        if (sort != null) {
            if (sort.equals("VIEWS")) {
                dtos.sort(Comparator.comparing(EventShortDto::getViews));
            }
            if (sort.equals("EVENT_DATE")) {
                dtos.sort(Comparator.comparing(EventShortDto::getEventDate));
            }
        }

        return dtos;
    }

    @Override
    public EventDto getById(long id, HttpServletRequest httpServletRequest) {
        EventDto dto = eventMapper.toDto(eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND)));

        dto.setViews(webClient.findView(id));
        webClient.addToStatistic(httpServletRequest);
        dto.setConfirmedRequests(requestRepository.getCountConfirmed(id));
        log.info("--==>>EVENTSERV public getById id=/{}/", id);
        return dto;
    }

    @Override
    public List<EventDto> getByUser(long id, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<EventDto> dtos = eventRepository.findAllByInitiatorId(id, pageable).getContent().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());

        ViewStatsDto[] viewStatsDtos = webClient.getViews(dtos.toArray(new EventDto[0]));
        for (ViewStatsDto view : viewStatsDtos) {
            log.info("--==>>substr uri = /{}/",
                    view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            Long viewId = Long.parseLong(view.getUri().substring(view.getUri().lastIndexOf("/") + 1));
            dtos.stream()
                    .filter(eventDto -> Objects.equals(eventDto.getId(), viewId))
                    .forEach(eventDto -> eventDto.setViews(Math.toIntExact(view.getHits())));
        }
        log.info("--==>>EVENTSERV query events by user id=/{}/", id);
        return dtos;
    }

    @Override
    @Transactional
    public Event update(long userId, Event event) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        Category category = new Category();
        if (event.getCategory().getId() != null) {
            category = categoryRepository.findById(event.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        }
        Event eventUpd = eventRepository.findByIdAndInitiator(event.getId(), user);
        if (eventUpd == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        if (event.getAnnotation() != null && !event.getAnnotation().isBlank()) {
            eventUpd.setAnnotation(event.getAnnotation());
        }
        if (event.getTitle() != null && !event.getAnnotation().isBlank()) {
            eventUpd.setTitle(event.getTitle());
        }
        if (event.getDescription() != null && !event.getAnnotation().isBlank()) {
            eventUpd.setDescription(event.getDescription());
        }
        if (event.getCreatedOn() != null) {
            eventUpd.setCreatedOn(event.getCreatedOn());
        }
        if (event.getEventDate() != null) {
            eventUpd.setEventDate(event.getEventDate());
        }
        if (event.getLatitude() != 0) {
            eventUpd.setLatitude(event.getLatitude());
        }
        if (event.getLongitude() != 0) {
            eventUpd.setLongitude(event.getLongitude());
        }
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
        Category category = new Category();
        if (event.getCategory().getId() != null) {
            category = categoryRepository.findById(event.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        }
        Event eventUpd = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND));
        if (eventUpd == null) {
            throw new NotFoundException(HttpStatus.NOT_FOUND);
        }
        if (event.getAnnotation() != null && !event.getAnnotation().isBlank()) {
            eventUpd.setAnnotation(event.getAnnotation());
        }
        if (event.getTitle() != null && !event.getAnnotation().isBlank()) {
            eventUpd.setTitle(event.getTitle());
        }
        if (event.getDescription() != null && !event.getAnnotation().isBlank()) {
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
    public EventUserDto getUserEvent(long userId, long eventId) {
        EventUserDto dto = eventMapper.toUserDto(eventRepository.findByIdAndInitiatorId(eventId, userId));
        dto.setViews(webClient.findView(eventId));
        dto.setConfirmedRequests(requestRepository.getCountConfirmed(eventId));
        log.info("---===>>> EVENTSERV query current user's event: userId=/{}/, eventId=/{}/", userId, eventId);
        return dto;
    }

    @Override
    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        event.setState(EventState.CANCELED);
        log.info("---===>>> EVENTSERV cancel current user's event: userId=/{}/, event=/{}/", userId, event);
        return event;
    }

    private List<EventsCountConfirmed> getConfirmed(List<Event> events) {
        List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        String sql = "select e.id id,count(e.id) cnt from events e join requests r on r.event_id=e.id " +
                "where r.status='CONFIRMED' and e.state='PUBLISHED' and e.id in (:ids) group by e.id";

        List<EventsCountConfirmed> result = jdbcTemplate.query(sql, parameters, new RowMapper<EventsCountConfirmed>() {
            @Override
            public EventsCountConfirmed mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new EventsCountConfirmed(rs.getLong("id"), rs.getInt("cnt"));
            }
        });
        log.info("--==>> EVENTSERV getCountConfirm param = /{}/, list=/{}/", ids, result);
        return result;
    }

}
