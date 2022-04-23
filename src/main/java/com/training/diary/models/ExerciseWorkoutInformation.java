package com.training.diary.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString(of = {"exerciseWorkoutId","information"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseWorkoutInformation {
    @EmbeddedId
    private ExerciseWorkoutId exerciseWorkoutId=new ExerciseWorkoutId();
    @ManyToOne
    @MapsId("exerciseId")
    private Exercise exercise;
    @ManyToOne
    @MapsId("workoutId")
    private Workout workout;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="information_id")
    private Information information;

    @Builder
    public ExerciseWorkoutInformation(Exercise exercise, Workout workout, Information information) {
        this.exercise = exercise;
        this.workout = workout;
        this.information = information;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class ExerciseWorkoutId implements Serializable {
        private Long exerciseId;
        private Long workoutId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof ExerciseWorkoutId))
                return false;

            ExerciseWorkoutId other = (ExerciseWorkoutId) o;

            return exerciseId != null &&
                    exerciseId.equals(other.getExerciseId()) &&
                    workoutId != null &&
                    workoutId.equals(other.getWorkoutId())
                    ;
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ExerciseWorkoutInformation))
            return false;

        ExerciseWorkoutInformation other = (ExerciseWorkoutInformation) o;

        return exerciseWorkoutId != null &&
                exerciseWorkoutId.equals(other.getExerciseWorkoutId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
