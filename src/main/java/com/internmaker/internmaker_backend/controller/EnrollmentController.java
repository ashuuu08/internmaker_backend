package com.internmaker.internmaker_backend.controller;

import com.internmaker.internmaker_backend.entity.Course;
import com.internmaker.internmaker_backend.entity.Enrollment;
import com.internmaker.internmaker_backend.entity.User;
import com.internmaker.internmaker_backend.repository.CourseRepository;
import com.internmaker.internmaker_backend.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/start")
    public ResponseEntity<?> startEnrollment(
            @RequestParam Long courseId,
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authentication");
        }

        // Correctly get the User entity
        User user = (User) authentication.getPrincipal();
        String email = user.getEmail(); // ✅ get email

        Enrollment enrollment;
        try {
            enrollment = enrollmentService.startEnrollment(courseId, email);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok(enrollment);
    }

}
