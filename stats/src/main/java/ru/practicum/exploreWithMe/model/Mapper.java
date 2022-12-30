package ru.practicum.exploreWithMe.model;

import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public EndpointHit toEndpoitHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp());
        return endpointHit;
    }

    public EndpointHitDto toDto(EndpointHit endpointHit) {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp(endpointHit.getApp());
        dto.setUri(endpointHit.getUri());
        dto.setIp(endpointHit.getIp());
        dto.setTimestamp(endpointHit.getTimestamp());
        return dto;
    }
}
