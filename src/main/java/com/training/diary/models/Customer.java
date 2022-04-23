package com.training.diary.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"id","firstName","lastName","email"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<Workout> workouts;

    @Builder
    private Customer(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void addWorkout(@NotNull Workout workout){
        if (workouts==null)
        {
            workouts=new ArrayList<>();
        }

        workouts.add(workout);
        workout.setCustomer(this);
    }

    public void removeWorkout(@NotNull Workout workout){
        if (workouts==null)
        {
            return;
        }

        workouts.remove(workout);
        workout.setCustomer(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Customer))
            return false;

        Customer other = (Customer) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
