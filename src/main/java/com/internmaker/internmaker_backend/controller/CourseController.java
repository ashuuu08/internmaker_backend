package com.internmaker.internmaker_backend.controller;

import com.internmaker.internmaker_backend.entity.Course;
import com.internmaker.internmaker_backend.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/active")
    public List<Course> getActiveCourses() {
        return courseService.getActiveCourses();
    }
}
