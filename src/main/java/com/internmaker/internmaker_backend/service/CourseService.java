package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.entity.Course;
import com.internmaker.internmaker_backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findByActiveTrue();
    }
}
