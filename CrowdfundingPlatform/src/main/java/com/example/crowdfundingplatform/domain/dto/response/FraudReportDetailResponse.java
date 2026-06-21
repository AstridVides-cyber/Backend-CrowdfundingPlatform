package com.example.crowdfundingplatform.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudReportDetailResponse {

    private Long id;
    private String reason;
    private Boolean resolved;
    private LocalDateTime createdAt;
    private Long reporterId;
    private String reporterName;
    private Long campaignId;
    private String campaignTitle;
}