package com.coffee_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for training application queries
 * Contains information about a farmer's application to a training program
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Training application information with participant details")
public class ApplicationQueryResponse {
    
    @Schema(description = "Unique identifier of the training application", example = "1")
    private Long applicationId;
    
    @Schema(description = "Unique identifier of the farmer who applied", example = "42")
    private Long farmerId;
    
    @Schema(description = "Name of the farmer who applied", example = "John Smith")
    private String farmerName;
    
    @Schema(description = "Current number of participants in the training program", example = "15")
    private int currentParticipants;
    
    @Schema(description = "Maximum capacity of participants for the training program", example = "30")
    private int maxParticipants;
    
    @Schema(description = "Date and time when the application was submitted", example = "2023-08-15 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applicationTime;
}
