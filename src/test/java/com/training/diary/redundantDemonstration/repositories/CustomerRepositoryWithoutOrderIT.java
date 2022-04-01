package com.training.diary.redundantDemonstration.repositories;

import com.training.diary.models.Customer;
import com.training.diary.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryWithoutOrderIT {

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

    CustomerRepositoryWithoutOrderIT() {
        customerUsman = Customer.builder().id(customerUsmanId).firstName(customerUsmanFirstName)
                .lastName(customerUsmanLastName).email(customerUsmanEmail).build();
        customerAadam = Customer.builder().id(customerAadamId).firstName(customerAadamFirstName)
                .lastName(customerAadamLastName).email(customerAadamEmail).build();
    }

    @Test
    void save() {
        customerRepository.save(customerUsman);
        customerRepository.save(customerAadam);
        assertEquals(2, customerRepository.count());
    }

    @Test
    void findById() {
        customerRepository.save(customerUsman);
        Optional<Customer> optionalCustomerUsman = customerRepository.findById(customerUsmanId);
        if (optionalCustomerUsman.isPresent()) {
            Customer customerUsmanFromOptional = optionalCustomerUsman.get();
            assertEquals(customerUsmanFromOptional, customerUsman);
        } else {
            fail("No customer with id " + customerUsmanId);
        }
    }

    @Test
    void deleteById() {
        customerRepository.save(customerUsman);
        if (customerRepository.existsById(customerUsmanId)) {
            customerRepository.deleteById(customerUsmanId);
            assertFalse(customerRepository.existsById(customerUsmanId));
        } else {
            fail("No customer to delete");
        }
    }

    @Test
    void deleteAll() {
        customerRepository.save(customerUsman);
        customerRepository.save(customerAadam);
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