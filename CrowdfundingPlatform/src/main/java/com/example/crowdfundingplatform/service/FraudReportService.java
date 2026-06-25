package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreateFraudReportRequest;
import com.example.crowdfundingplatform.domain.dto.response.FraudReportDetailResponse;

import java.util.List;

public interface FraudReportService {
    FraudReportDetailResponse createReport(CreateFraudReportRequest request, String reporterEmail);
    List<FraudReportDetailResponse> getUnresolvedReports();
    FraudReportDetailResponse resolveReport(Long id);
}