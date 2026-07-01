package com.example.crowdfundingplatform.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDetailResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal minimumAmount;
    private Integer quantity;
    private Long campaignId;
    private String campaignTitle;
}