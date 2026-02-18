package com.internmaker.internmaker_backend.repository;

import com.internmaker.internmaker_backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(User user);

    Optional<Enrollment> findByUserAndCourseAndStatus(User user, Course course, EnrollmentStatus status);

    // New methods for dashboard
    List<Enrollment> findByUserEmail(String email);

    long countByStatus(EnrollmentStatus status);
}