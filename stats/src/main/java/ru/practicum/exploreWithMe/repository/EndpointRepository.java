package ru.practicum.exploreWithMe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.model.EndpointHit;
import ru.practicum.exploreWithMe.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EndpointRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("select new ru.practicum.exploreWithMe.model.ViewStats(count(e.ip), e.app, e.uri) " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end and e.uri in :uris group by e.app,e.uri")
    Collection<ViewStats> getAll(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.exploreWithMe.model.ViewStats(count (distinct e.ip), e.app, e.uri) " +
            "from EndpointHit e " +
            "where e.timestamp between :start and :end and e.uri in :uris group by e.app,e.uri")
    Collection<ViewStats> getAllWithUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);
}
