package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.entity.*;
import com.internmaker.internmaker_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RazorpayService razorpayService;

    // STEP 1: Create Order
    public Enrollment createEnrollmentOrder(Long courseId, String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        // Check duplicates
        enrollmentRepository.findByUserAndCourseAndStatus(user, course, EnrollmentStatus.CONFIRMED)
                .ifPresent(e -> { throw new RuntimeException("Already enrolled"); });

        // Call Razorpay
        String orderResponse = razorpayService.createOrder(course.getPrice());
        JSONObject orderJson = new JSONObject(orderResponse);
        String razorpayOrderId = orderJson.getString("id");

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .amount(course.getPrice())
                .status(EnrollmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderId(razorpayOrderId)
                .build();

        return enrollmentRepository.save(enrollment);
    }
}