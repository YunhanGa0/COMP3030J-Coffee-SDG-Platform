package com.coffee_backend.dto;

import com.coffee_backend.enumType.ApplicationStatus;
import lombok.Data;

@Data
public class FinancialReviewRequest {
    private ApplicationStatus status;
    private String adminFeedback;
}

