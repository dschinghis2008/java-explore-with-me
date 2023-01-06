package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Request;
import ru.practicum.exploreWithMe.model.dto.RequestDto;
import ru.practicum.exploreWithMe.model.mapper.RequestMapper;
import ru.practicum.exploreWithMe.service.RequestService;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping("/users/{userId}/requests")
    public RequestDto add(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestMapper.toDto(requestService.add(userId, eventId));
    }

    @PatchMapping("users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestMapper.toDto(requestService.cancel(userId, requestId));
    }

    @GetMapping("/users/{userId}/requests")
    public Collection<RequestDto> getAll(@PathVariable Long userId) {
        Collection<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : requestService.getAll(userId)) {
            requestDtos.add(requestMapper.toDto(request));
        }
        return requestDtos;
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getAllOfAuthor(@PathVariable Long userId, @PathVariable Long eventId) {
        Collection<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : requestService.getAllOfAuthor(userId, eventId)) {
            requestDtos.add(requestMapper.toDto(request));
        }
        return requestDtos;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirm(@PathVariable Long userId, @PathVariable Long eventId,
                              @PathVariable Long reqId) {
        return requestMapper.toDto(requestService.confirm(userId, eventId, reqId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto reject(@PathVariable Long userId, @PathVariable Long eventId,
                             @PathVariable Long reqId) {
        return requestMapper.toDto(requestService.reject(userId, eventId, reqId));
    }
}
