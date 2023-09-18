package ru.practicum.model.paticipation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.event.Event;
import ru.practicum.model.paticipation.Participation;
import ru.practicum.model.user.User;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findAllByRequester(User user);

    List<Participation> findByRequesterAndEvent(User user, Event event);
}
