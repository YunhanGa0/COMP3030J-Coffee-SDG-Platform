package com.coffee_backend.dto;

import com.coffee_backend.enumType.TrainingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainingStatusRequest {
    private TrainingStatus status;
}
