package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.repository.EndpointRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndpointServiceImpl implements EndpoinService {
    private final EndpointRepository repository;

    @Override
    public EndpointHit add(EndpointHit endpointHit) {
        log.info("--==>>ENDPOINT_CONTR added hit=/{}/", endpointHit);
        return repository.save(endpointHit);
    }

    @Override
    public Collection<ViewStats> getAll(String start, String end, String[] uris, Boolean unique) {
        LocalDateTime startDt;
        LocalDateTime endDt;
        if (start != null && end != null) {
            startDt = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            endDt = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            startDt =
                    LocalDateTime.parse(URLDecoder.decode("1971-01-01 00:00:00", StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            endDt =
                    LocalDateTime.parse(URLDecoder.decode("2101-01-01 00:00:00", StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        Collection<ViewStats> result = new ArrayList<>();
        if (unique) {
            result = repository.getAllWithUniqueIp(startDt, endDt, uris);
        } else {
            result = repository.getAll(startDt, endDt, uris);
        }
        return result;
    }
}
