package com.coffee_backend.dto;

import com.coffee_backend.enumType.CertificationStatus;
import lombok.Data;

@Data
public class CertificationReviewRequest {
    private CertificationStatus status;
    private String adminFeedback;
}
