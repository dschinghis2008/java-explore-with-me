package ru.practicum.exploreWithMe.service.webClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.exploreWithMe.model.dto.EventShortDto;
import ru.practicum.exploreWithMe.model.dto.HitDto;
import ru.practicum.exploreWithMe.model.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
@Slf4j
public class WebClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String statUrl;

    public WebClient(@Value("${stats-server.url}") String statUrl) {
        this.statUrl = statUrl;
    }

    public void addToStatistic(HttpServletRequest httpServletRequest, String appName) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(appName);
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        String url = statUrl + "/hit";
        log.info("---===>>>WEBCLIENT hitDto=/{}/, url=/{}/", hitDto, url);
        HttpEntity<HitDto> request = new HttpEntity<>(hitDto);
        restTemplate.postForObject(url, request, HitDto.class);
    }

    public Integer findView(Long eventId) {
        ViewStatsDto[] views = restTemplate.getForObject(statUrl + "/stats?uris=/events/"
                + eventId.toString(), ViewStatsDto[].class);
        if (views != null) {
            if (views.length > 0) {
                return views[0].getHits().intValue();
            }
        }
        return null;

    }

    public ViewStatsDto[] getViews(EventShortDto[] dtos) {
        StringBuilder sb = new StringBuilder(statUrl + "/stats?uris=");
        for (int i = 0; i < dtos.length; i++) {
            if (i > 0 && i < dtos.length - 1) {
                sb.append(",");
            }
            sb.append("/events/" + dtos[i].getId());
        }
        return restTemplate.getForObject(sb.toString(), ViewStatsDto[].class);
    }

}
