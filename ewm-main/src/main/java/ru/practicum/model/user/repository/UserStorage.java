package ru.practicum.model.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.user.User;
import ru.practicum.utility.database.OffsetBasedPageRequest;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmRequestParameterConflict;
import ru.practicum.utility.exceptions.EwmSQLContraintViolation;
import ru.practicum.utility.exceptions.EwmSQLFailedException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStorage {

    @Autowired
    private final UserRepository repository;

    @Transactional
    public User saveUser(User user) {
        log.debug("LEVEL - Storage. METHOD - saveUser. Entered");
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new EwmRequestParameterConflict(exception.getMessage());
        }
    }

    @Transactional
    public void removeUser(Long userId) {
        log.debug("LEVEL - Storage. METHOD - removeUser. Entered");
        try {
            repository.delete(getUser(userId));
        } catch (Exception exception) {
            log.info("LEVEL - Storage. METHOD - removeUser. User with id = "
                    + userId + " wasn't deleted. Cause " + exception.getMessage());
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        log.debug("LEVEL - Storage. METHOD - getUser. Entered");
        return repository.findById(userId).orElseThrow(
                () -> {
                    log.info("LEVEL - Storage. METHOD - getUser. User with id = " + userId + " was not found");
                    throw new EwmEntityNotFoundException("User with id=" + userId + " was not found");
                }
        );
    }

    @Transactional(readOnly = true)
    public List<User> getUsers(int from, int size) {
        log.debug("LEVEL - Storage. METHOD - getUsers. Entered");
        return repository.findAll(new OffsetBasedPageRequest(from, size)).toList();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByListId(List<Long> ids) {
        log.debug("LEVEL - Storage. METHOD - getUsersByListId. Entered");
        try {
            return repository.findAllById(ids);
        } catch (Exception ex) {
            log.info("LEVEL - Storage. METHOD - getUser. Failed to read users from database");
            throw new EwmSQLFailedException("Failed to read users from database");
        }
    }

}
