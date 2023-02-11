package ru.practicum.exploreWithMe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "requester_id"}))
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User requester;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Request(Long id, LocalDateTime created, Event event, User requester, Status status) {
        this.id = id;
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
