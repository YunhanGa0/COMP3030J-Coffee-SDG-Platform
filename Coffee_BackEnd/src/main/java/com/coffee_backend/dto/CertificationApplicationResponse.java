package com.coffee_backend.dto;

import com.coffee_backend.entity.Farm;
import com.coffee_backend.enumType.CertificationStatus;
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
public class CertificationApplicationResponse {
    private Long id;
    private Long farmId;
    private String description;
    private CertificationStatus status;
    private String adminFeedback;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;
}
