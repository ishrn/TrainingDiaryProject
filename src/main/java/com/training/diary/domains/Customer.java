package com.training.diary.domains;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
