package ru.practicum.ewm.stat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm.stat.model.StatLogEntry;
import ru.practicum.ewm.stat.repo.StatEntryRepository;
import ru.practicum.ewm.stat.utility.PersistenceConfig;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@Slf4j
@DataJpaTest
@Import({PersistenceConfig.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:prescript.sql")
public class RepositoryTests {

    @Autowired
    StatEntryRepository testRepository;

    @Autowired
    private TestEntityManager testEM;

    @Test
    void saveToRepoTest() {
        StatLogEntry testEntry = testRepository.save(
                new StatLogEntry(null, "testUri", "testApp", "testIP", LocalDateTime.now()));

        assertEquals(1L, testEntry.getId());
        assertEquals("testUri", testEntry.getUri());
        assertEquals("testApp", testEntry.getApp());
        assertEquals("testIP", testEntry.getIp());
    }
}
