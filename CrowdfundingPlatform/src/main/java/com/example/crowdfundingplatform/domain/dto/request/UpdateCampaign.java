package com.example.crowdfundingplatform.domain.dto.request;

import com.example.crowdfundingplatform.domain.enums.GoalType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateCampaign {

    private String title;

    private String description;

    private BigDecimal goal;

    private GoalType goalType;

    private LocalDateTime deadline;

    private String category;

    private String location;

    private Boolean isFeatured;


}
