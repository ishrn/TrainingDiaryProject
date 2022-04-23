package com.training.diary.repositories;

import com.training.diary.models.Information;
import org.springframework.data.repository.CrudRepository;

public interface InformationRepository extends CrudRepository<Information,Long> {
}
