package com.training.diary.repositories;

import com.training.diary.models.ExerciseWorkoutInformation;
import com.training.diary.models.ExerciseWorkoutInformation.ExerciseWorkoutId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseWorkoutInformationRepository extends JpaRepository<ExerciseWorkoutInformation, ExerciseWorkoutId> {
}
