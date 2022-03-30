package com.training.diary.domains;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
