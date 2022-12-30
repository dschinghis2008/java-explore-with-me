package ru.practicum.exploreWithMe.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.dto.RequestDto;
import ru.practicum.exploreWithMe.service.EventService;
import ru.practicum.exploreWithMe.service.UserService;

@Service
@RequiredArgsConstructor
public class RequestMapper {

    private final UserService userService;
    private final EventService eventService;
    public RequestDto toDto(Request request){
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

    public Request toRequest(RequestDto requestDto){
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setCreated(requestDto.getCreated());
        request.setEvent(eventService.getById(requestDto.getEvent()));
        request.setRequester(userService.getById(requestDto.getRequester()));
        request.setStatus(requestDto.getStatus());
        return request;
    }
}
