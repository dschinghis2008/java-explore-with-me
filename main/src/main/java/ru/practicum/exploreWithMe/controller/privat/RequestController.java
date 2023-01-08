package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.dto.RequestDto;
import ru.practicum.exploreWithMe.model.mapper.RequestMapper;
import ru.practicum.exploreWithMe.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping("/users/{userId}/requests")
    public RequestDto add(@PathVariable long userId, @RequestParam long eventId) {
        return requestMapper.toDto(requestService.add(userId, eventId));
    }

    @PatchMapping("users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancel(@PathVariable long userId, @PathVariable long requestId) {
        return requestMapper.toDto(requestService.cancel(userId, requestId));
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getAll(@PathVariable long userId) {
        return requestService.getAll(userId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getAllOfAuthor(@PathVariable long userId, @PathVariable long eventId) {
        return requestService.getAllOfAuthor(userId, eventId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirm(@PathVariable long userId, @PathVariable long eventId,
                              @PathVariable long reqId) {
        return requestMapper.toDto(requestService.confirm(userId, eventId, reqId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto reject(@PathVariable long userId, @PathVariable long eventId,
                             @PathVariable long reqId) {
        return requestMapper.toDto(requestService.reject(userId, eventId, reqId));
    }
}
