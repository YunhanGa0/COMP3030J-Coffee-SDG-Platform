package com.coffee_backend.dto;

import com.coffee_backend.entity.FinancialSupport;
import com.coffee_backend.enumType.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialApplicationResponse {
    private Long id;
    private FinancialSupport financialSupport;
    private ApplicationStatus status;
    private String purpose;
    private Long farmerId;
    private String farmerUsername;
    private String adminFeedback;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;
}
