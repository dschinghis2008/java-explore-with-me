package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;
import ru.practicum.exploreWithMe.model.Status;

import java.util.List;

@Data
public class RequestsUpd {
    private final List<Long> requestIds;
    private final Status status;
}
