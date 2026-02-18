package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.entity.Course;
import com.internmaker.internmaker_backend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;

    // Admin creates a course
    public Course createCourse(Course course) {
        course.setActive(true); // Default to active
        return repository.save(course);
    }

    // Public gets all active courses
    public List<Course> getAllActiveCourses() {
        return repository.findByActiveTrue();
    }
}