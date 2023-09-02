package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.StatEntry;

public interface StatRepository extends JpaRepository<StatEntry, Long> {
}
