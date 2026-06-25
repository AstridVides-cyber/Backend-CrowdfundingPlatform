package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.dto.response.FraudReportDetailResponse;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.service.CampaignService;
import com.example.crowdfundingplatform.service.FraudReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CampaignService campaignService;
    private final FraudReportService fraudReportService;

    @GetMapping("/campaigns/pending")
    public ResponseEntity<List<CampaignDetailResponse>> getPendingCampaigns() {
        return ResponseEntity.ok(campaignService.getCampaignsByStatus(CampaignStatus.PENDING));
    }

    @PatchMapping("/campaigns/{id}/approve")
    public ResponseEntity<CampaignDetailResponse> approveCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.approveCampaign(id));
    }

    @PatchMapping("/campaigns/{id}/reject")
    public ResponseEntity<CampaignDetailResponse> rejectCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.rejectCampaign(id));
    }

    @GetMapping("/fraud-reports")
    public ResponseEntity<List<FraudReportDetailResponse>> getUnresolvedReports() {
        return ResponseEntity.ok(fraudReportService.getUnresolvedReports());
    }

    @PatchMapping("/fraud-reports/{id}/resolve")
    public ResponseEntity<FraudReportDetailResponse> resolveReport(@PathVariable Long id) {
        return ResponseEntity.ok(fraudReportService.resolveReport(id));
    }
}
