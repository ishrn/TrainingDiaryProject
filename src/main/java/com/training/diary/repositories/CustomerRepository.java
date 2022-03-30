package com.training.diary.repositories;

import com.training.diary.domains.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Long> {
}
