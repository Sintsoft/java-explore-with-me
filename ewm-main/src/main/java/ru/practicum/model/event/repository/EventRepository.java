package ru.practicum.model.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.event.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
