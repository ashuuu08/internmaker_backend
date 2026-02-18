package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.dto.DashboardStats;
import com.internmaker.internmaker_backend.entity.Enrollment;
import com.internmaker.internmaker_backend.entity.EnrollmentStatus;
import com.internmaker.internmaker_backend.entity.User;
import com.internmaker.internmaker_backend.repository.CourseRepository;
import com.internmaker.internmaker_backend.repository.EnrollmentRepository;
import com.internmaker.internmaker_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    /**
     * Get dashboard statistics for a specific user
     */
    public Map<String, Object> getUserDashboard(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> dashboard = new HashMap<>();

        // User Info
        dashboard.put("userId", user.getId());
        dashboard.put("fullName", user.getFullName());
        dashboard.put("email", user.getEmail());
        dashboard.put("phone", user.getPhone());
        dashboard.put("role", user.getRole().name());

        // Enrollment Stats
        List<Enrollment> userEnrollments = enrollmentRepository.findByUserEmail(email);

        long totalEnrollments = userEnrollments.size();
        long pendingEnrollments = userEnrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.PENDING)
                .count();
        long confirmedEnrollments = userEnrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.CONFIRMED)
                .count();

        dashboard.put("totalEnrollments", totalEnrollments);
        dashboard.put("pendingEnrollments", pendingEnrollments);
        dashboard.put("confirmedEnrollments", confirmedEnrollments);
        dashboard.put("hasActiveEnrollment", confirmedEnrollments > 0);

        // Course Stats
        long totalCourses = courseRepository.count();
        long enrolledCourses = confirmedEnrollments;
        long availableCourses = totalCourses - enrolledCourses;

        dashboard.put("totalCourses", totalCourses);
        dashboard.put("enrolledCourses", enrolledCourses);
        dashboard.put("availableCourses", availableCourses);

        // Progress Stats (placeholder - can be enhanced)
        dashboard.put("completedTasks", 0);
        dashboard.put("totalTasks", 12);
        dashboard.put("progressPercentage", 0);
        dashboard.put("certificateEligible", false);

        return dashboard;
    }

    /**
     * Get admin dashboard statistics
     */
    public DashboardStats getAdminStats() {
        long totalEnrollments = enrollmentRepository.count();
        long pendingEnrollments = enrollmentRepository.countByStatus(EnrollmentStatus.PENDING);
        long confirmedEnrollments = enrollmentRepository.countByStatus(EnrollmentStatus.CONFIRMED);

        return new DashboardStats(
                totalEnrollments,
                pendingEnrollments,
                confirmedEnrollments,
                confirmedEnrollments // enrolled = confirmed
        );
    }

    /**
     * Get user's enrollments
     */
    public List<Enrollment> getUserEnrollments(String email) {
        return enrollmentRepository.findByUserEmail(email);
    }
}
