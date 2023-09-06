package ru.practicum.models.user.vault;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.models.user.model.User;
import ru.practicum.utility.database.OffsetBasedPageRequest;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmSQLContraintViolation;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStroage {

    @Autowired
    private final UserRepository repository;

    public User saveUser(User savedUser) {
        try {
            return repository.save(savedUser);
        } catch (ConstraintViolationException exception) {
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    public List<User> getUsers(int from, int size) {
        return repository.findAll(new OffsetBasedPageRequest(from, size)).toList();
    }

    public User getUser(long id) {
        return repository.findById(id).orElseThrow(
                () -> {throw new EwmEntityNotFoundException("User with id=" + id + " was not found");}
        );
    }

    public void removeUser(long id) {
        repository.delete(getUser(id));
    }
}
