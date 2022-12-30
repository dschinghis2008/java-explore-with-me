package ru.practicum.exploreWithMe.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String annotation;

    private String title;

    private String description;

    @Column(name = "created")
    private LocalDateTime createdOn;

    @Column(name = "dt")
    private LocalDateTime eventDate;

    @Column(name = "event_lat")
    private Double latitude;

    @Column(name = "event_lon")
    private Double longitude;

    private Boolean paid;

    @Column(name = "participant_max")
    private Integer participantLimit;

    @Column(name = "published_dt")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User initiator;
}
