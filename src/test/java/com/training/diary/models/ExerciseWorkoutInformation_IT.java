package com.training.diary.models;

import com.training.diary.models.Exercise;
import com.training.diary.models.ExerciseWorkoutInformation;
import com.training.diary.models.ExerciseWorkoutInformation.ExerciseWorkoutId;
import com.training.diary.models.Information;
import com.training.diary.models.Workout;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExerciseWorkoutInformation_IT {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private Long exerciseId;
    private Long workoutId;
    private Long informationId;
    private ExerciseWorkoutId exerciseWorkoutInformationId;

    @BeforeAll
    public void setupBeforeAll(){
        TransactionStatus transaction = platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
        Exercise exercise = Exercise.builder().build();
        Workout workout = Workout.builder().build();
        Information information = new Information();
        ExerciseWorkoutInformation exerciseWorkoutInformation = ExerciseWorkoutInformation.builder().exercise(exercise).workout(workout).information(information).build();
        entityManager.persist(exercise);
        entityManager.persist(workout);
        entityManager.persist(information);
        entityManager.persist(exerciseWorkoutInformation);
        entityManager.flush();
        platformTransactionManager.commit(transaction);
        exerciseId=exercise.getId();
        workoutId=workout.getId();
        informationId=information.getId();
        exerciseWorkoutInformationId=exerciseWorkoutInformation.getExerciseWorkoutId();
    }

    @AfterAll
    public void setupAfterAll(){
        if (entityManager!=null&&entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Test
    public void should_returnOne_whenExerciseWorkoutInformationsSizeRequested(){
        assertEquals(1,entityManager.find(Exercise.class, exerciseId).getExerciseWorkoutInformations().size());
        assertEquals(1,entityManager.find(Workout.class, workoutId).getExerciseWorkoutInformations().size());
    }

    @Test
    public void should_removeInformationAndNotRemoveWorkout_whenExerciseDeleted(){
        Exercise exercise = entityManager.find(Exercise.class, exerciseId);
        entityManager.remove(exercise);
        entityManager.flush();
        entityManager.clear();
        assertNotEquals(null,entityManager.find(Workout.class,workoutId));
        assertEquals(null,entityManager.find(Information.class, informationId));
    }

    @Test
    public void should_removeInformationAndNotRemoveExercise_whenWorkoutDeleted(){
        Workout workout = entityManager.find(Workout.class, workoutId);
        entityManager.remove(workout);
        entityManager.flush();
        entityManager.clear();
        assertNotEquals(null,entityManager.find(Exercise.class, exerciseId));
        assertEquals(null,entityManager.find(Information.class, informationId));
    }

    @Test
    public void should_NotRemoveExerciseAndWorkout_whenInformationDeleted(){
        Information information = entityManager.find(Information.class, informationId);
        ExerciseWorkoutInformation exerciseWorkoutInformation = entityManager.find(ExerciseWorkoutInformation.class, exerciseWorkoutInformationId);
        exerciseWorkoutInformation.setInformation(null);
        entityManager.remove(information);
        entityManager.flush();
        entityManager.clear();
        assertNotEquals(null,entityManager.find(Exercise.class, exerciseId));
        assertNotEquals(null,entityManager.find(Workout.class, workoutId));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_throwLazyException_whenImagesAndVideosRequested() {
        assertThrows(LazyInitializationException.class, () -> {
            entityManager.find(Exercise.class, exerciseId).getExerciseWorkoutInformations().size();
                });
        assertThrows(LazyInitializationException.class, () -> {
            entityManager.find(Workout.class, workoutId).getExerciseWorkoutInformations().size();
        });
    }

}
