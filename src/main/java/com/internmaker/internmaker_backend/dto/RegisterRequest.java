package com.internmaker.internmaker_backend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role; // "STUDENT"

    // Payment Verification Fields
    private String orderId;
    private String paymentId;
    private String signature;
}