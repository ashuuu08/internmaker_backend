package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.dto.*;
import com.internmaker.internmaker_backend.entity.*;
import com.internmaker.internmaker_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RazorpayService razorpayService;

    public AuthResponse register(RegisterRequest request) {
        // 1. Verify Payment
        boolean isPaymentValid = false;

        if ("LINK_PAYMENT".equals(request.getOrderId())) {
            // If using external link, verify the manual paymentId via API
            isPaymentValid = razorpayService.verifyPaymentId(request.getPaymentId(), 1.0); // Reverted to 1.0 for
                                                                                           // testing
        } else {
            // Standard Modal Signature verification
            isPaymentValid = razorpayService.verifySignature(
                    request.getOrderId(),
                    request.getPaymentId(),
                    request.getSignature());
        }

        if (!isPaymentValid) {
            throw new RuntimeException(
                    "Payment verification failed! The Transaction ID is invalid or the amount paid does not match ₹1.0.");
        }

        // 2. Already exists check (Prevent multiple registrations with same email)
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("This email (" + request.getEmail()
                    + ") is already registered. Please login or use a different email.");
        }

        // 3. Proceed with Registration
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.valueOf(request.getRole()))
                .build();
        repository.save(user);

        System.out.println("SUCCESS: User " + user.getEmail() + " registered successfully.");

        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, user.getFullName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        // ✅ FIX: Pass the User object (which implements UserDetails) instead of just
        // the username
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, user.getFullName(), user.getRole().name());
    }
}