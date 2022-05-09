package com.training.diary.repositories;

import com.training.diary.models.Category;
import com.training.diary.models.Workout;
import com.training.diary.utilities.StringUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public interface WorkoutRepository extends CrudRepository<Workout,Long> {

    @Query("SELECT DISTINCT e.category FROM Exercise e INNER JOIN ExerciseWorkoutInformation ewi ON e.id=ewi.exercise INNER JOIN Workout w ON ewi.workout=w.id WHERE w.id=:workoutId")
    List<Category> getCategories(Long workoutId);

    default String getCategoriesAsSortedString(Long workoutId){
        List<Category> categories = getCategories(workoutId);
        categories.sort(Comparator.comparing(Enum::name));
        StringJoiner stringJoiner = new StringJoiner(", ");
        for(Category category:categories) {
            stringJoiner.add(StringUtils.capitalize(category.name()));
        }

        return stringJoiner.toString();
    }
}
