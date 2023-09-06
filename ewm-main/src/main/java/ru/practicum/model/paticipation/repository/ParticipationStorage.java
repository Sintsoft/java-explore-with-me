package ru.practicum.model.paticipation.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.event.Event;
import ru.practicum.model.paticipation.Participation;
import ru.practicum.model.paticipation.enums.ParticipationRequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmSQLContraintViolation;
import ru.practicum.utility.exceptions.EwmSQLFailedException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationStorage {

    @Autowired
    private final ParticipationRepository repository;

    @Autowired
    private final SessionFactory sessionFactory;

    @Transactional
    public Participation saveParticipation(Participation participation) {
        log.debug("LEVEL - Storage. METHOD - saveParticipation. Entered");
        try {
            return repository.save(participation);
        } catch (DataIntegrityViolationException exception) {
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional
    public List<Participation> saveParticipationsList(List<Participation> participations) {
        log.debug("LEVEL - Storage. METHOD - saveParticipation. Entered");
        try {
            return repository.saveAll(participations);
        } catch (DataIntegrityViolationException exception) {
            throw new EwmSQLContraintViolation(exception.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Participation getParticipation(Long id) {
        log.debug("LEVEL - Storage. METHOD - getParticipation. Entered");
        return repository.findById(id).orElseThrow(
                () -> {
                    log.info("LEVEL - Storage. METHOD - getCategory. Request with id = " + id + " was not found");
                    throw new EwmEntityNotFoundException("Request with id=" + id + " was not found");
                }
        );
    }

    @Transactional(readOnly = true)
    public List<Participation> getPartisipationsList(List<Long> ids) {
        log.debug("LEVEL - Storage. METHOD - getPartisipationsList. Entered");
        try {
            return repository.findAllById(ids);
        } catch (Exception ex) {
            log.info("LEVEL - Storage. METHOD - getPartisipationsList. Failed to read requests from database");
            throw new EwmSQLFailedException("Failed to read requests from database. cause: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public boolean alreadyRequested(User user, Event event) {
        return repository.findByRequesterAndEvent(user, event).isEmpty();
    }

    @Transactional(readOnly = true)
    public List<Participation> getUserParticipations(User user) {
        return repository.findAllByRequester(user);
    }

    @Transactional(readOnly = true)
    public List<Participation> getEventsParticipations(List<Event> events, Boolean confirmed) {
        if (events.isEmpty()) return List.of();
        try (Session session = sessionFactory.openSession();) {

            CriteriaBuilder builder = session.getCriteriaBuilder();

            CriteriaQuery<Participation> criteriaQuery = builder.createQuery(Participation.class);
            Root<Participation> root = criteriaQuery.from(Participation.class);

            criteriaQuery.select(root);

            List<Predicate> predicates = new LinkedList<>();
            predicates.add(root.get("event").in(events));
            if (confirmed != null) {
                    predicates.add(builder.equal(
                            root.get("status").as(ParticipationRequestStatus.class),
                            confirmed ? ParticipationRequestStatus.CONFIRMED : ParticipationRequestStatus.REJECTED));
            }
            criteriaQuery.select(root).where(builder.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            log.warn("searchForEvents - " + ex.getMessage());
            throw new EwmSQLFailedException("Failed to find event due to: " + ex.getMessage());
        }
    }


}
