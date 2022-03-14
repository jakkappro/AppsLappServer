package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    List<Exercise> findAll();
}