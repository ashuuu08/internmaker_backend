package com.internmaker.internmaker_backend.entity;

public enum EnrollmentStatus {
    PENDING,   // User clicked enroll, waiting for payment
    CONFIRMED, // Payment successful (PAID)
    FAILED     // Payment failed
}