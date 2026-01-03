package com.internmaker.internmaker_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Course course;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    // STEP 2
    private LocalDateTime createdAt;

    // STEP 3 (payment order)
    private String razorpayOrderId;

    // STEP 5 (payment confirmation)
    private String paymentId;
    private LocalDateTime paidAt;

    // ===== SETTERS =====

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setAmount(Double amount) {
        this.amount = amount;

    } @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
