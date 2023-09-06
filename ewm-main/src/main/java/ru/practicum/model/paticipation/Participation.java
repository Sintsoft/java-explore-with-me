package ru.practicum.model.paticipation;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.Event;
import ru.practicum.model.paticipation.enums.ParticipationRequestStatus;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "participations")
public class Participation {

    @Column(name = "creation_time")
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;

    public Participation(Event event, User user) {
        this.requester = user;
        this.event = event;
    }

}
