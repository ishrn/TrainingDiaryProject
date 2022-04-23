package com.training.diary.repositories;

import com.training.diary.models.Workout;
import org.springframework.data.repository.CrudRepository;

public interface WorkoutRepository extends CrudRepository<Workout,Long> {
}
