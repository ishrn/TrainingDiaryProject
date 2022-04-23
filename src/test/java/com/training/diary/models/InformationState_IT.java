package com.training.diary.models;

import com.training.diary.models.Information;
import com.training.diary.models.State;
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
public class InformationState_IT {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private Long informationId;

    public State getState(){
        State state = State.builder().reps(3).weight(12.5F).build();
        return state;
    }

    @BeforeAll
    public void setupBeforeAll(){
        TransactionStatus transaction = platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
        Information information = new Information();
        entityManager.persist(information);
        platformTransactionManager.commit(transaction);
        informationId=information.getId();
    }

    @AfterAll
    public void setupAfterAll(){
        if (entityManager!=null&&entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @Test
    public void should_returnOne_whenStatesSizeRequested(){
        Information information = entityManager.find(Information.class, informationId);
        State state = getState();
        information.addState(state);
        entityManager.flush();
        Long stateId=state.getId();
        entityManager.clear();

        assertEquals(1,entityManager.find(Information.class, informationId).getStates().size());
        assertNotEquals(null,entityManager.find(State.class,stateId));
    }

    @Test
    public void should_returnZero_whenWorkoutsSizeRequested(){
        Information information = entityManager.find(Information.class, informationId);
        State state = getState();
        information.addState(state);
        entityManager.flush();
        Long stateId=state.getId();
        entityManager.clear();

        information = entityManager.find(Information.class, informationId);
        state = entityManager.find(State.class, stateId);
        information.removeState(state);
        entityManager.persist(information);

        entityManager.flush();
        entityManager.clear();

        assertEquals(0,entityManager.find(Information.class, informationId).getStates().size());
        assertEquals(null,entityManager.find(State.class,stateId));
    }

    @Test
    public void should_returnNull_whenWorkoutRequested(){
        Information information = entityManager.find(Information.class, informationId);
        State state = getState();
        information.addState(state);
        entityManager.flush();
        Long stateId=state.getId();
        entityManager.clear();

        information= entityManager.find(Information.class, informationId);
        entityManager.remove(information);
        entityManager.flush();
        entityManager.clear();

        assertEquals(null,entityManager.find(State.class,stateId));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_throwLazyException_whenWorkoutsRequested() {
        assertThrows(LazyInitializationException.class, () -> {
                    entityManager.find(Information.class, 1L).getStates().size();
                });
    }

}
