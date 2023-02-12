package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestsUpdResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
