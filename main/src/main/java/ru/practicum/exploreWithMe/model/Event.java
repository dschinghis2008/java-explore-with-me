package ru.practicum.exploreWithMe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "created")
    private LocalDateTime createdOn;

    @Column(name = "dt", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event_lat", nullable = false)
    private Double latitude;

    @Column(name = "event_lon", nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_max", nullable = false)
    private Integer participantLimit;

    @Column(name = "published_dt")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventState state;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User initiator;

    public Event(Long id, String annotation, String title, String description, LocalDateTime createdOn,
                 LocalDateTime eventDate, Double latitude, Double longitude, Boolean paid, Integer participantLimit,
                 LocalDateTime publishedOn, Boolean requestModeration, EventState state,
                 Category category, User initiator) {
        this.id = id;
        this.annotation = annotation;
        this.title = title;
        this.description = description;
        this.createdOn = createdOn;
        this.eventDate = eventDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.category = category;
        this.initiator = initiator;
    }

}
