package com.training.diary.services;

import com.training.diary.models.Customer;

import java.util.Optional;

public interface CustomerService {
    Customer save(Customer entity);

    Optional<Customer> findById(Long id);

    boolean existsById(Long id);

    Iterable<Customer> findAll();

    long count();

    void deleteById(Long id);

    void delete(Customer entity);

    void deleteAll();
}
