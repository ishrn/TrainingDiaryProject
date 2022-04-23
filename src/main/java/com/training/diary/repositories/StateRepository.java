package com.training.diary.repositories;

import com.training.diary.models.State;
import org.springframework.data.repository.CrudRepository;

public interface StateRepository extends CrudRepository<State,Long> {
}
