package com.example.crowdfundingplatform.domain.dto.response;

import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.domain.enums.GoalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDetailResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal goal;
    private GoalType goalType;
    private LocalDateTime deadline;
    private CampaignStatus status;
    private String category;
    private String location;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private Long creatorId;
    private String creatorName;
}