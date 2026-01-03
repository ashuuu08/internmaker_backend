package com.internmaker.internmaker_backend.repository;

import com.internmaker.internmaker_backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByUserAndCourseAndStatus(
            User user,
            Course course,
            EnrollmentStatus status
    );
}
