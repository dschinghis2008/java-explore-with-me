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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000)
    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Event event;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private Boolean visible;

    public Comment(Long id, String text, User author, Event event, LocalDateTime created, Boolean visible) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.event = event;
        this.created = created;
        this.visible = visible;
    }
}
