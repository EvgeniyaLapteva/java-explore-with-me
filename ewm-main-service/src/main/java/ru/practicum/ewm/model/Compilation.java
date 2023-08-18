package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "events_compilations",
    joinColumns = @JoinColumn(name = "compilation_id"),
    inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    private List<Event> events = new ArrayList<>();

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false)
    private String title;
}
