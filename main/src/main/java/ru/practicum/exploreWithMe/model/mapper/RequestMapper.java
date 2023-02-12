package ru.practicum.exploreWithMe.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.dto.RequestDto;
import ru.practicum.exploreWithMe.model.dto.RequestsUpdResult;

import java.util.List;
import java.util.stream.Collectors;

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

    public RequestsUpdResult toReqUpdRes(List<Request> confirmed, List<Request> rejected) {
        RequestsUpdResult result = new RequestsUpdResult();
        if (confirmed != null) {
            result.setConfirmedRequests(confirmed.stream().map(this::toDto).collect(Collectors.toList()));
        }
        if (rejected != null) {
            result.setRejectedRequests(rejected.stream().map(this::toDto).collect(Collectors.toList()));
        }
        return result;
    }

}
