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

    @Size(max = 1000)
    private String annotation;

    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    @Column(name = "created")
    private LocalDateTime createdOn;

    @Column(name = "dt", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event_lat")
    private double latitude;

    @Column(name = "event_lon")
    private double longitude;

    private boolean paid;

    @Column(name = "participant_max")
    private int participantLimit;

    @Column(name = "published_dt")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventState state;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User initiator;

    public Event(Long id, String annotation, String title, String description, LocalDateTime createdOn,
                 LocalDateTime eventDate, Double latitude, Double longitude, boolean paid, int participantLimit,
                 LocalDateTime publishedOn, boolean requestModeration, EventState state, Category category, User initiator) {
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

    public boolean getPaid() {
        return paid;
    }

    public boolean getRequestModeration() {
        return requestModeration;
    }
}
