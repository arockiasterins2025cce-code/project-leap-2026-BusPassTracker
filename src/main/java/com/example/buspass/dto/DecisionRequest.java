package com.example.buspass.dto;

public record DecisionRequest(
        String remark,
        String rejectionReason,
        Integer validityDays
) {}
