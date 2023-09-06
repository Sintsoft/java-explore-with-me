package ru.practicum.model.category.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.category.Category;
import ru.practicum.utility.database.OffsetBasedPageRequest;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmRequestParameterConflict;
import ru.practicum.utility.exceptions.EwmSQLContraintViolation;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryStorage {

    @Autowired
    private final CategoryRepository repository;

    @Transactional
    public Category saveCategory(Category category) {
        log.debug("LEVEL - Storage. METHOD - saveCategory. Entered");
        try {
            return repository.save(category);
        } catch (DataIntegrityViolationException | ConstraintViolationException exception) {
            throw new EwmRequestParameterConflict(exception.getMessage());
        }
    }

    @Transactional
    public void removeCategory(Long catId) {
        log.debug("LEVEL - Storage. METHOD - removeCategory. Entered");
        try {
            repository.delete(getCategory(catId));
        } catch (Exception exception) {
            log.info("LEVEL - Storage. METHOD - v. Category with id = "
                    + catId + " wasn't deleted. Cause " + exception.getMessage());
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories(int from, int size) {
        log.debug("LEVEL - Storage. METHOD - getCategories. Entered");
        return repository.findAll(new OffsetBasedPageRequest(from, size)).toList();
    }

    @Transactional(readOnly = true)
    public List<Category> getCategoriesByListId(List<Long> ids) {
        log.debug("LEVEL - Storage. METHOD - getCategories. Entered");
        return repository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long catId) {
        log.debug("LEVEL - Storage. METHOD - getCategory. Entered");
        return repository.findById(catId).orElseThrow(
                () -> {
                    log.info("LEVEL - Storage. METHOD - getCategory. Category with id = " + catId + " was not found");
                    throw new EwmEntityNotFoundException("User with id=" + catId + " was not found");
                }
        );
    }
}
