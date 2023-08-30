package ru.practicum.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.StatisticEntry;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntry, Long> {
}
