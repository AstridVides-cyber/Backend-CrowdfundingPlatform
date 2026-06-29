package com.example.crowdfundingplatform.mapper;

import com.example.crowdfundingplatform.domain.dto.request.CreateFraudReportRequest;
import com.example.crowdfundingplatform.domain.dto.response.FraudReportDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.FraudReport;
import com.example.crowdfundingplatform.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FraudReportMapper {

    public FraudReport toEntity(CreateFraudReportRequest req, User reporter, Campaign campaign) {
        return FraudReport.builder()
                .reason(req.getReason())
                .resolved(false)
                .reporter(reporter)
                .campaign(campaign)
                .build();
    }

    public FraudReportDetailResponse toResponse(FraudReport report) {
        return FraudReportDetailResponse.builder()
                .id(report.getId())
                .reason(report.getReason())
                .resolved(report.getResolved())
                .createdAt(report.getCreatedAt())
                .reporterId(report.getReporter().getId())
                .reporterName(report.getReporter().getName())
                .campaignId(report.getCampaign().getId())
                .campaignTitle(report.getCampaign().getTitle())
                .build();
    }

    public List<FraudReportDetailResponse> toListResponse(List<FraudReport> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
