package ru.practicum.exploreWithMe.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User requester;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
