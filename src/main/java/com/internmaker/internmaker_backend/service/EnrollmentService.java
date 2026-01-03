package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.entity.*;
import com.internmaker.internmaker_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public Enrollment startEnrollment(Long courseId, String email) {

        // 1. Fetch user from JWT email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // 3. Block duplicate CONFIRMED enrollment
        enrollmentRepository
                .findByUserAndCourseAndStatus(user, course, EnrollmentStatus.CONFIRMED)
                .ifPresent(e -> {
                    throw new RuntimeException("Already enrolled in this course");
                });

        // 4. Create PENDING enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setAmount(course.getPrice());
        enrollment.setStatus(EnrollmentStatus.PENDING);
        enrollment.setCreatedAt(LocalDateTime.now());

        // 5. Save & return
        return enrollmentRepository.save(enrollment);
    }
}
