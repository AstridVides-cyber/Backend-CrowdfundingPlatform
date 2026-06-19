package com.example.crowdfundingplatform.domain.dto.response;

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
public class PledgeDetailResponse {

    private Long id;
    private BigDecimal amount;
    private Boolean charged;
    private Boolean refunded;
    private LocalDateTime createdAt;
    private Long sponsorId;
    private String sponsorName;
    private Long campaignId;
    private String campaignTitle;
    private Long rewardId;
    private String rewardTitle;
}