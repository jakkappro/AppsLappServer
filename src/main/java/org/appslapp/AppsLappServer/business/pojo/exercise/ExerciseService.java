package org.appslapp.AppsLappServer.business.pojo.exercise;

import org.appslapp.AppsLappServer.persistance.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(@Autowired ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public long save(Exercise exercise) {
        return exerciseRepository.save(exercise).getId();
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }
}