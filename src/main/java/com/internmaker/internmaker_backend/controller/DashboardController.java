package com.internmaker.internmaker_backend.controller;

import com.internmaker.internmaker_backend.dto.DashboardStats;
import com.internmaker.internmaker_backend.entity.Enrollment;
import com.internmaker.internmaker_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get current user's dashboard data
     * Endpoint: GET /api/dashboard/me
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        Map<String, Object> dashboard = dashboardService.getUserDashboard(email);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Get current user's enrollments
     * Endpoint: GET /api/dashboard/enrollments
     */
    @GetMapping("/enrollments")
    public ResponseEntity<List<Enrollment>> getMyEnrollments(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<Enrollment> enrollments = dashboardService.getUserEnrollments(email);
        return ResponseEntity.ok(enrollments);
    }

    /**
     * Get admin statistics (Admin only)
     * Endpoint: GET /api/dashboard/admin/stats
     */
    @GetMapping("/admin/stats")
    public ResponseEntity<DashboardStats> getAdminStats() {
        DashboardStats stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(stats);
    }
}
