package ru.practicum.models.user.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.models.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
