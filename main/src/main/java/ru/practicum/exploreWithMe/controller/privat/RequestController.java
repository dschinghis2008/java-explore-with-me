package ru.practicum.exploreWithMe.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.model.Status;
import ru.practicum.exploreWithMe.model.dto.RequestDto;
import ru.practicum.exploreWithMe.model.dto.RequestsUpd;
import ru.practicum.exploreWithMe.model.dto.RequestsUpdResult;
import ru.practicum.exploreWithMe.model.mapper.RequestMapper;
import ru.practicum.exploreWithMe.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<RequestDto> add(@PathVariable long userId, @RequestParam @Valid @NotNull long eventId) {
        log.info("--==>>REQ_CTRL_before_add_request:userId=/{}/,eventId=/{}/", userId, eventId);
        RequestDto result = requestMapper.toDto(requestService.add(userId, eventId));
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancel(@PathVariable long userId, @PathVariable long requestId) {
        return requestMapper.toDto(requestService.cancel(userId, requestId));
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getAll(@PathVariable long userId,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        return requestService.getAll(userId, from, size).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getAllOfAuthor(@PathVariable long userId, @PathVariable long eventId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return requestService.getAllOfAuthor(userId, eventId, from, size).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public RequestsUpdResult update(@PathVariable long userId, @PathVariable long eventId,
                                    @RequestBody RequestsUpd requestsUpd) {
        log.info("--==>>REQ_CTRL update requestUpd=/{}/", requestsUpd);
        if (requestsUpd.getStatus().equals(Status.REJECTED)) {
            return requestService.reject(userId, eventId, requestsUpd);
        } else {
            return requestService.confirm(userId, eventId, requestsUpd);
        }
    }
}
