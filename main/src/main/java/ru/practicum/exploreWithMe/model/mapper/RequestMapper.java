package ru.practicum.exploreWithMe.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.dto.RequestDto;

@Component
public class RequestMapper {

    public RequestDto toDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

}
