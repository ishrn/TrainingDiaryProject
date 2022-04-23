package com.training.diary.models;

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
public class CustomerWorkout_IT {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private Long customerId;

    @BeforeAll
    public void setupBeforeAll(){
        TransactionStatus transaction = platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
        Customer customer = Customer.builder().build();
        entityManager.persist(customer);
        platformTransactionManager.commit(transaction);
        customerId=customer.getId();
    }

    @AfterAll
    public void setupAfterAll(){
        if (entityManager!=null&&entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Test
    public void should_returnOne_whenWorkoutsSizeRequested(){
        Customer customer = entityManager.find(Customer.class, customerId);
        Workout workout = Workout.builder().build();
        customer.addWorkout(workout);
        entityManager.flush();
        Long workoutId= workout.getId();
        entityManager.clear();

        assertEquals(1,entityManager.find(Customer.class, customerId).getWorkouts().size());
        assertNotEquals(null,entityManager.find(Workout.class, workoutId));
    }

    @Test
    public void should_returnZero_whenWorkoutsSizeRequested(){
        Customer customer = entityManager.find(Customer.class, customerId);
        Workout workout = Workout.builder().build();
        customer.addWorkout(workout);
        entityManager.flush();
        Long workoutId=workout.getId();
        entityManager.clear();

        customer = entityManager.find(Customer.class, customerId);
        workout = entityManager.find(Workout.class,workoutId);
        customer.removeWorkout(workout);
        entityManager.flush();
        entityManager.clear();

        assertEquals(0,entityManager.find(Customer.class, customerId).getWorkouts().size());
        assertEquals(null,entityManager.find(Workout.class, workoutId));
    }

    @Test
    public void should_returnNull_whenWorkoutRequested(){
        Customer customer = entityManager.find(Customer.class, customerId);
        Workout workout = Workout.builder().build();
        customer.addWorkout(workout);
        entityManager.flush();
        Long workoutId=workout.getId();
        entityManager.clear();

        customer = entityManager.find(Customer.class, customerId);
        entityManager.remove(customer);
        entityManager.flush();
        entityManager.clear();

        assertEquals(null,entityManager.find(Workout.class,workoutId));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_throwLazyException_whenWorkoutsRequested() {
        assertThrows(LazyInitializationException.class, () -> {
                    entityManager.find(Customer.class, 1L).getWorkouts().size();
                });
    }

}
