package com.training.diary.redundantDemonstration.services;

import com.training.diary.models.Customer;
import com.training.diary.repositories.CustomerRepository;
import com.training.diary.services.CustomerService;
import com.training.diary.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    CustomerRepository customerRepository;
    CustomerService customerService;

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

    final private Long count=2L;

    CustomerServiceImplTest() {
        customerUsman = Customer.builder().id(customerUsmanId).firstName(customerUsmanFirstName)
                .lastName(customerUsmanLastName).email(customerUsmanEmail).build();
        customerAadam = Customer.builder().id(customerAadamId).firstName(customerAadamFirstName)
                .lastName(customerAadamLastName).email(customerAadamEmail).build();
    }

    @BeforeEach
    void setup(){
        customerRepository= Mockito.mock(CustomerRepository.class);
        customerService=new CustomerServiceImpl(customerRepository);
    }

    @Test
    void save() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customerUsman);

        assertEquals(customerService.save(customerUsman),customerUsman);
    }

    @Test
    void findById() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customerUsman));

        //noinspection OptionalGetWithoutIsPresent
        assertEquals(customerService.findById(customerUsmanId).get(),customerUsman);
    }

    @Test
    void existsById() {
        when(customerRepository.existsById(any(Long.class))).thenReturn(true);

        assertTrue(customerService.existsById(customerUsmanId));
    }

    @Test
    void findAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(customerUsman);
        customers.add(customerAadam);
        when(customerRepository.findAll()).thenReturn(customers);

        assertEquals(StreamSupport.stream(customerService.findAll().spliterator(),false).count(),count);
    }

    @Test
    void count() {
        when(customerRepository.count()).thenReturn(count);
        assertEquals(customerService.count(),count);
    }

    @Test
    void deleteById() {
        long id = anyLong();
        customerService.deleteById(id);
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void delete() {
        Customer customer = any();
        customerService.delete(customer);
        Mockito.verify(customerRepository).delete(customer);
    }

    @Test
    void deleteAll() {
        Customer customer = any(Customer.class);
        customerService.delete(customer);
        Mockito.verify(customerRepository).delete(customer);
    }
}