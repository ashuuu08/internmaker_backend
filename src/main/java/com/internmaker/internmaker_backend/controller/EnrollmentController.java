package com.internmaker.internmaker_backend.controller;

import com.internmaker.internmaker_backend.entity.Enrollment;
import com.internmaker.internmaker_backend.entity.EnrollmentStatus;
import com.internmaker.internmaker_backend.repository.EnrollmentRepository;
import com.internmaker.internmaker_backend.service.EnrollmentService;
import com.internmaker.internmaker_backend.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final RazorpayService razorpayService;
    private final EnrollmentRepository enrollmentRepository;

    @PostMapping("/create/{courseId}")
    public ResponseEntity<?> createOrder(@PathVariable Long courseId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Enrollment enrollment = enrollmentService.createEnrollmentOrder(courseId, userDetails.getUsername());
            return ResponseEntity.ok(Map.of(
                    "orderId", enrollment.getOrderId(),
                    "enrollmentId", enrollment.getId(),
                    "amount", enrollment.getAmount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> data) {
        String orderId = data.get("razorpay_order_id");
        String paymentId = data.get("razorpay_payment_id");
        String signature = data.get("razorpay_signature");
        Long enrollmentId = Long.parseLong(data.get("enrollmentId"));

        boolean isValid = razorpayService.verifySignature(orderId, paymentId, signature);

        if (isValid) {
            Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
            enrollment.setStatus(EnrollmentStatus.CONFIRMED);
            enrollment.setTransactionId(paymentId);
            enrollmentRepository.save(enrollment);
            return ResponseEntity.ok("Payment Verified");
        }
        return ResponseEntity.badRequest().body("Verification Failed");
    }
}