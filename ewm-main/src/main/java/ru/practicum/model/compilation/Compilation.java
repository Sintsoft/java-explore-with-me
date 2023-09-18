package ru.practicum.model.compilation;

import lombok.Data;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "compilation_events",
            joinColumns = { @JoinColumn(name = "compilation_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") }
    )
    private List<Event> events;
}
