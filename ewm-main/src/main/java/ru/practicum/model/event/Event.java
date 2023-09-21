package ru.practicum.model.event;

import lombok.Data;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.enums.EventStates;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "events")
public class Event {

    /*
    * Shared with request fields
    * */

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "paid")
    private Boolean paid = false;

    @Column(name = "moderation")
    private Boolean requestModeration = true;

    @Column(name = "participant_limit")
    private Integer participantLimit = 0;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    /*
    * Shared with respose fields
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Enumerated(EnumType.STRING)
    @Column(name = "statement")
    private EventStates state = EventStates.PENDING;

    @Column(name = "creation_time")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "publication_time")
    private LocalDateTime publishedOn;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_liked_event",
            joinColumns = { @JoinColumn(name = "event_id")},
            inverseJoinColumns = { @JoinColumn(name = "liker_id")}
    )
    private Set<User> likers = new HashSet<>();

    /*
    * Inner fields
    * */

    @Column(name = "lattitude")
    private Float lattitude;

    @Column(name = "longtitude")
    private Float longtitude;

    @Column(name = "sended")
    private boolean sended = false;

    public static class LikerComparator implements Comparator<Set> {

        @Override
        public int compare(Set set1, Set set2) {
            return set1.size() - set2.size();
        }
    }
}
