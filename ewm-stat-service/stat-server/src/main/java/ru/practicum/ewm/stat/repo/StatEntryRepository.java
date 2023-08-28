package ru.practicum.ewm.stat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.stat.model.StatLogEntry;

public interface StatEntryRepository extends JpaRepository<StatLogEntry, Long> {
}
