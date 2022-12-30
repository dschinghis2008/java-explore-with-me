package ru.practicum.exploreWithMe.model.dto;

import lombok.Data;

@Data
public class HitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
