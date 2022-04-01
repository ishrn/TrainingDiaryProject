package com.training.diary.redundantDemonstration.bootstrap;

import com.training.diary.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql({"classpath:data-h2.sql"})
@ActiveProfiles("test")
public class BootstrapTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void customerDataInitialized() {
        assertTrue(customerRepository.count() > 0);
    }
}
