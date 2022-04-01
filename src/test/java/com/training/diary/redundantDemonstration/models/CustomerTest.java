package com.training.diary.redundantDemonstration.models;

import com.training.diary.models.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    final private Long customerFergusId = 1L;
    final private Long customerFergusAnotherId = 2L;
    final private String customerFergusFirstName = "Fergus";
    final private String customerFergusLastName = "Archer";
    final private String customerFergusEmail = "fergus_archer@mail.com";

    @Test
    void creationTest() {
        Customer customerFergus = Customer.builder().id(customerFergusId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        assertTrue(customerFergus.getId().equals(customerFergusId)&&customerFergus.getFirstName().equals(customerFergusFirstName)&&
                customerFergus.getLastName().equals(customerFergusLastName)&&customerFergus.getEmail().equals(customerFergusEmail));
    }

    @Test
    void equalsTest() {
        Customer customerFergus1 = Customer.builder().id(customerFergusId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        Customer customerFergus2 = Customer.builder().id(customerFergusId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        assertEquals(customerFergus1,customerFergus2);
    }

    @Test
    void notEqualsTest() {
        Customer customerFergus1 = Customer.builder().id(customerFergusId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        Customer customerFergus2 = Customer.builder().id(customerFergusAnotherId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        assertNotEquals(customerFergus1,customerFergus2);
    }

    @Test
    void hashCodeTest() {
        Customer customerFergus1 = Customer.builder().id(customerFergusId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        Customer customerFergus2 = Customer.builder().id(customerFergusId).firstName(customerFergusFirstName).lastName(customerFergusLastName).email(customerFergusEmail).build();
        System.out.println(customerFergus1.hashCode());
        System.out.println(customerFergus2.hashCode());
        assertEquals(customerFergus1.hashCode(),customerFergus2.hashCode());
    }
}