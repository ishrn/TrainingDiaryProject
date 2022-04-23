package com.training.diary.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Workout_IT {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private Long workoutId;

    @Test
    @Execution(ExecutionMode.SAME_THREAD)
    @ResourceLock(value = Resources.TIME_ZONE)
    // 1. Time should be persisted in database with milliseconds
    // 2. This test should be run single, without affecting other tests, because of changing default timezone
    // 3. Timezones used for test:
    //   a. America/Rainy_River timezone - UTC-05:00
    //   b. Asia/Aqtau timezone          - UTC+05:00
    public void should_returnAsiaAqtauTimeZoneTime_whenDateRequested(){
        TimeZone defaultTimeZone = TimeZone.getDefault();

        try{
            ZoneId america = ZoneId.of("America/Rainy_River");
            ZoneId asia = ZoneId.of("Asia/Aqtau");
            OffsetDateTime offsetDateTimeAmerica = OffsetDateTime.now(america);
            Workout workout = Workout.builder().date(offsetDateTimeAmerica).build();
            entityManager.persist(workout);
            entityManager.flush();
            entityManager.clear();
            workoutId=workout.getId();

            TimeZone.setDefault(TimeZone.getTimeZone(asia));
            workout = entityManager.find(Workout.class, workoutId);

            assertEquals(0,offsetDateTimeAmerica.plusHours(10).truncatedTo(ChronoUnit.SECONDS).toLocalDateTime()
                    .compareTo(workout.getDate().truncatedTo(ChronoUnit.SECONDS).toLocalDateTime()));
        }
        catch (Exception exception){
            throw exception;
        }
        finally {
            TimeZone.setDefault(defaultTimeZone);
        }
    }
}
