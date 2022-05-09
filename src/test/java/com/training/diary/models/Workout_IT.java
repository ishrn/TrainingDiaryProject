package com.training.diary.models;

import com.training.diary.repositories.WorkoutRepository;
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

    @Autowired
    private WorkoutRepository workoutRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
            Long workoutId=workout.getId();

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

    @Test
    public void should_returnCategoriesString_WhenCategoriesRequested(){
        Exercise exercise1 = Exercise.builder().category(Category.LEGS).build();
        Exercise exercise2 = Exercise.builder().category(Category.LEGS).build();
        Exercise exercise3 = Exercise.builder().category(Category.ARMS).build();
        entityManager.persist(exercise1);
        entityManager.persist(exercise2);
        entityManager.persist(exercise3);

        Customer customer = Customer.builder().build();

        Workout workout1 = Workout.builder().build();
        ExerciseWorkoutInformation exerciseWorkoutInformation1 = ExerciseWorkoutInformation.builder().workout(workout1).exercise(exercise1).build();
        ExerciseWorkoutInformation exerciseWorkoutInformation2 = ExerciseWorkoutInformation.builder().workout(workout1).exercise(exercise2).build();
        ExerciseWorkoutInformation exerciseWorkoutInformation3 = ExerciseWorkoutInformation.builder().workout(workout1).exercise(exercise3).build();

        entityManager.persist(customer);
        entityManager.persist(exerciseWorkoutInformation1);
        entityManager.persist(exerciseWorkoutInformation2);
        entityManager.persist(exerciseWorkoutInformation3);

        entityManager.flush();
        entityManager.clear();

        assertEquals(2,workoutRepository.getCategories(workout1.getId()).size());
        assertEquals("Arms, Legs",workoutRepository.getCategoriesAsSortedString(workout1.getId()));
    }
}
