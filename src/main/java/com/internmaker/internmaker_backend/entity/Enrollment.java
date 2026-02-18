package com.internmaker.internmaker_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Links to Student

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; // Links to Course

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private Double amount;

    private String transactionId; // Razorpay Payment ID
    private String orderId;       // Razorpay Order ID
}