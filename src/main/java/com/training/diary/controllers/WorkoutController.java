package com.training.diary.controllers;

import com.training.diary.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;

    @GetMapping("workout")
    public String workout(Model model){
        model.addAttribute("workouts",workoutRepository.findAll());
        return "workout_list";
    }

    @GetMapping("check")
    public String check(){
        return "footer";
    }

    @GetMapping("index")
    public String index(){
        return "index";
    }

}
