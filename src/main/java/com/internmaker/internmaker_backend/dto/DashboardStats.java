package com.internmaker.internmaker_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStats {
    private long totalEnrollments;
    private long pendingEnrollments;
    private long approvedEnrollments;
    private long enrolledEnrollments;
}