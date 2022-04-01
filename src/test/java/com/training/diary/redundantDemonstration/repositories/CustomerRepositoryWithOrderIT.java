package com.training.diary.redundantDemonstration.repositories;

import com.training.diary.models.Customer;
import com.training.diary.repositories.CustomerRepository;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
class CustomerRepositoryWithOrderIT {

    final private Long customerUsmanId = 1L;
    final private String customerUsmanFirstName = "Usman";
    final private String customerUsmanLastName = "Powell";
    final private String customerUsmanEmail = "usman_powell@mail.com";
    final private Long customerAadamId = 2L;
    final private String customerAadamFirstName = "Aadam";
    final private String customerAadamLastName = "Black";
    final private String customerAadamEmail = "aadam_black@mail.com";

    final private Customer customerUsman;
    final private Customer customerAadam;

    @Autowired
    private CustomerRepository customerRepository;

    CustomerRepositoryWithOrderIT() {
        customerUsman = Customer.builder().id(customerUsmanId).firstName(customerUsmanFirstName)
                .lastName(customerUsmanLastName).email(customerUsmanEmail).build();
        customerAadam = Customer.builder().id(customerAadamId).firstName(customerAadamFirstName)
                .lastName(customerAadamLastName).email(customerAadamEmail).build();
    }

    @Test
    @Order(0)
    void repositoryEmpty() {
        assertFalse(savedCustomersExist(), "No customers should be saved yet");
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    void save() {
        customerRepository.save(customerUsman);
        customerRepository.save(customerAadam);
        assertEquals(2, customerRepository.count());
    }

    @Test
    @Order(2)
    void findById() {
        Optional<Customer> optionalCustomerUsman = customerRepository.findById(customerUsmanId);
        if (optionalCustomerUsman.isPresent()) {
            Customer customerUsmanFromOptional = optionalCustomerUsman.get();
            assertEquals(customerUsmanFromOptional, customerUsman);
        } else {
            fail("No customer with id " + customerUsmanId);
        }
    }

    @Test
    @Order(3)
    void deleteById() {
        if (customerRepository.existsById(customerUsmanId)) {
            customerRepository.deleteById(customerUsmanId);
            assertFalse(customerRepository.existsById(customerUsmanId));
        } else {
            fail("No customer to delete");
        }
    }

    @Test
    @Order(4)
    void deleteAll() {
        if (savedCustomersExist()) {
            customerRepository.deleteAll();
            assertEquals(0, customerRepository.count());
        } else {
            fail("No customers were saved before the test");
        }
    }

    boolean savedCustomersExist() {
        return customerRepository.count() > 0;
    }

}