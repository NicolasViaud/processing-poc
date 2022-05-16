package com.processing.tracker.repositories;

import com.processing.tracker.domains.Tracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@DataJpaTest
class TrackerRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TrackerRepository trackerRepository;

    private Tracker tracker1;

    @BeforeEach
    private void init() {
        tracker1 = new Tracker();
        tracker1.setProcessingId("111");
        tracker1.setLastRefresh(ZonedDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
        tracker1.setProgress(5);
        testEntityManager.persist(tracker1);
    }

    @AfterEach
    private void remove() {
        testEntityManager.remove(tracker1);
    }

    @Test
    public void incrementProgressByProcessingIdTest1() {

        trackerRepository.incrementProgressByProcessingId(
                "111",
                10,
                20,
                ZonedDateTime.of(2022, 1, 1, 0, 0, 10, 0, ZoneOffset.UTC).toInstant()
        );

        final Tracker result = testEntityManager.refresh(tracker1);

        Assertions.assertEquals(15, result.getProgress());
        Assertions.assertEquals(25, result.getMaxProgress());
        Assertions.assertEquals(1000, result.getVelocity());
        Assertions.assertEquals(ZonedDateTime.of(2022, 1, 1, 0, 0, 10, 0, ZoneOffset.UTC).toInstant(), result.getLastRefresh());
    }

}