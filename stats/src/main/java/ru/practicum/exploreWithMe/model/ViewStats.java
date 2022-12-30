package ru.practicum.exploreWithMe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewStats {
    private Long hits;
    private String app;
    private String uri;


    public ViewStats(Long hits, String app, String uri ) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
