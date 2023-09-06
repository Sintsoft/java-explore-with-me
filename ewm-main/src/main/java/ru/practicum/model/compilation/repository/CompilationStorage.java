package ru.practicum.model.compilation.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.utility.database.OffsetBasedPageRequest;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmSQLContraintViolation;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompilationStorage {

    @Autowired
    private final CompilationRepository repository;

    public Compilation saveCompilation(Compilation compilation) {
        log.debug("LEVEL - Storage. METHOD - saveCompilation. Entered");
        try {
            return repository.save(compilation);
        } catch (DataIntegrityViolationException exception) {
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional
    public void removeCompilation(Long compId) {
        log.debug("LEVEL - Storage. METHOD - removeUser. Entered");
        try {
            repository.delete(getCompilation(compId));
        } catch (Exception exception) {
            log.info("LEVEL - Storage. METHOD - removeUser. User with id = "
                    + compId + " wasn't deleted. Cause " + exception.getMessage());
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Compilation getCompilation(Long compId) {
        log.debug("LEVEL - Storage. METHOD - getUser. Entered");
        return repository.findById(compId).orElseThrow(
                () -> {
                    log.info("LEVEL - Storage. METHOD - getUser. User with id = " + compId + " was not found");
                    throw new EwmEntityNotFoundException("User with id=" + compId + " was not found");
                }
        );
    }

    public List<Compilation> getCompilations(int from, int size) {
        log.debug("LEVEL - Storage. METHOD - getCompilations. Entered");
        return repository.findAll(new OffsetBasedPageRequest(from, size)).toList();
    }

    @Transactional(readOnly = true)
    public List<Compilation> getCompilationsByPinned(boolean pinned, int from, int size) {
        log.debug("LEVEL - Storage. METHOD - getCompilationsByPinned. Entered");
        return repository.findByPinned(pinned, new OffsetBasedPageRequest(from, size));
    }

}
