package ru.practicum.model.event.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dao.EventViewEntity;
import ru.practicum.model.event.enums.EventSorts;
import ru.practicum.model.event.enums.EventStates;
import ru.practicum.model.user.User;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmSQLContraintViolation;
import ru.practicum.utility.exceptions.EwmSQLFailedException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStorage {

    @Autowired
    private final EventRepository repository;

    @Autowired
    private final SessionFactory sessionFactory;

    @Transactional
    public Event saveEvent(Event event) {
        log.debug("LEVEL - Storage. METHOD - saveEvent. Entered");
        try {
            return repository.save(event);
        } catch (DataIntegrityViolationException exception) {
            log.debug("LEVEL - Storage. METHOD - saveEvent. Failed save event, cause: " + exception.getMessage());
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Event getEvent(Long eventId) {
        log.debug("LEVEL - Storage. METHOD - getEvent. Entered");
        return repository.findById(eventId).orElseThrow(
                () -> {
                    log.info("LEVEL - Storage. METHOD - getEvent. Event with id = " + eventId + " was not found");
                    throw new EwmEntityNotFoundException("Event with id=" + eventId + " was not found");
                }
        );
    }

    @Transactional(readOnly = true)
    public List<Event> getEventsByListId(List<Long> ids) {
        log.debug("LEVEL - Storage. METHOD - getEventsByListId. Entered");
        try {
            return ids == null || ids.isEmpty() ? List.of() : repository.findAllById(ids);
        } catch (Exception ex) {
            log.info("LEVEL - Storage. METHOD - getEventsByListId. Failed to read compilations from database");
            throw new EwmSQLFailedException("Failed to read events from database");
        }
    }

    @Transactional
    public void removeEvent(Long eventId) {
        log.debug("LEVEL - Storage. METHOD - removeEvent. Entered");
        try {
            repository.delete(getEvent(eventId));
        } catch (Exception exception) {
            log.info("LEVEL - Storage. METHOD - removeEvent. Event with id = "
                    + eventId + " wasn't deleted. Cause " + exception.getMessage());
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Event> searchForEvents(List<User> users,
                                       List<Category> categories,
                                       List<EventStates> states,
                                       String searchString,
                                       Boolean paid,
                                       Boolean avaliable,
                                       EventSorts sort,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       int from,
                                       int size,
                                       boolean admin/* админский запрос */,
                                       boolean isPublic) {
        try (Session session = sessionFactory.openSession();) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
            Root<Event> root = criteriaQuery.from(Event.class);

            List<Predicate> predicates = new LinkedList<>();
            if (isPublic) {
                predicates.add(builder.isNotNull(root.get("publishedOn").as(LocalDateTime.class)));
            }
            /*if (admin) {
                predicates.add(builder.isTrue(root.get("sended")));
            }*/
            if (users != null && !users.isEmpty()) {
                predicates.add(root.get("initiator").in(users));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(root.get("state").as(EventStates.class).in(states));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }
            if (start != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));
            }
            if (end != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));
            }
            if (searchString != null && !searchString.isBlank()) {
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("title").as(String.class)),
                        "%" + searchString.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("annotation").as(String.class)),
                        "%" + searchString.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description").as(String.class)),
                        "%" + searchString.toLowerCase() + "%")));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }

            criteriaQuery.select(root).where(builder.and(predicates.toArray(Predicate[]::new)));
            if (sort != null) {
                criteriaQuery.orderBy(builder.asc(root.get(sort.getCode())));
            }
            Query<Event> query = session.createQuery(criteriaQuery);
            List<Event> events = query.setFirstResult(from).setMaxResults(size).getResultList();
            return events;
        } catch (SQLGrammarException ex) {
            log.warn("searchForEvents - " + ex.getMessage() + "\n" + ex.getStackTrace().toString());
            throw new EwmSQLFailedException("Failed to find events due to: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<EventViewEntity> search(List<User> users,
                                        List<Category> categories,
                                        List<EventStates> states,
                                        String searchString,
                                        Boolean paid,
                                        Boolean avaliable,
                                        EventSorts sort,
                                        LocalDateTime start,
                                        LocalDateTime end,
                                        int from,
                                        int size,
                                        boolean isPublic) {
        try (Session session = sessionFactory.openSession();) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<EventViewEntity> criteriaQuery = builder.createQuery(EventViewEntity.class);
            Root<EventViewEntity> root = criteriaQuery.from(EventViewEntity.class);
            List<Predicate> predicates = new LinkedList<>();

            if (isPublic) {
                predicates.add(builder.isNotNull(root.get("publishedOn").as(LocalDateTime.class)));
            }
            /*if (admin) {
                predicates.add(builder.isTrue(root.get("sended")));
            }*/
            if (users != null && !users.isEmpty()) {
                predicates.add(root.get("initiator").in(users));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(root.get("state").as(EventStates.class).in(states));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }
            if (start != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));
            }
            if (end != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));
            }
            if (searchString != null && !searchString.isBlank()) {
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("title").as(String.class)),
                                "%" + searchString.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("annotation").as(String.class)),
                                "%" + searchString.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description").as(String.class)),
                                "%" + searchString.toLowerCase() + "%")));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }

            criteriaQuery.select(root).where(builder.and(predicates.toArray(Predicate[]::new)));
            if (sort != null) {
                switch (sort) {
                    case LIKES: criteriaQuery.orderBy(builder.desc(root.get(sort.getCode()))); break;
                    case EVENT_DATE: criteriaQuery.orderBy(builder.asc(root.get(sort.getCode()))); break;
                    default: criteriaQuery.orderBy(builder.desc(root.get("id"))); break;
                }
            }

            return session.createQuery(criteriaQuery)
                    .setFirstResult(from)
                    .setMaxResults(size).getResultList();
        } catch (SQLGrammarException ex) {
            log.warn("searchForEvents - " + ex.getMessage() + "\n" + ex.getStackTrace().toString());
            throw new EwmSQLFailedException("Failed to find events due to: " + ex.getMessage());
        }
    }
}
