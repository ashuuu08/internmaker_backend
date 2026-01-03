package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.dto.AuthResponse;
import com.internmaker.internmaker_backend.dto.LoginRequest;
import com.internmaker.internmaker_backend.dto.RegisterRequest;
import com.internmaker.internmaker_backend.entity.Role;
import com.internmaker.internmaker_backend.entity.User;
import com.internmaker.internmaker_backend.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role
        );

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                );

        authenticationManager.authenticate(authentication);

        String token = jwtService.generateToken(request.getEmail());

        return new AuthResponse(token);
    }
}
