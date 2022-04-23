package com.training.diary.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Clob;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"id", "date"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @Lob
    private Clob commentary;
    @Column(columnDefinition = "TIMESTAMP(6) WITHOUT TIME ZONE")
    private OffsetDateTime date;

    @OneToMany(mappedBy = "workout",cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private List<ExerciseWorkoutInformation> exerciseWorkoutInformations=new ArrayList<>();

    @Builder
    public Workout(Customer customer, Clob commentary, OffsetDateTime date) {
        this.customer = customer;
        this.commentary = commentary;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Workout))
            return false;

        Workout other = (Workout) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
