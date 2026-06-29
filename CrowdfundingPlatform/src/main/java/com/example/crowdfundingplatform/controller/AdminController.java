package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.dto.response.GeneralResponse;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.service.CampaignService;
import com.example.crowdfundingplatform.service.FraudReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<GeneralResponse> getUnresolvedReports() {
        return buildResponse("Reportes sin resolver obtenidos", HttpStatus.OK, fraudReportService.getUnresolvedReports());
    }

    @PatchMapping("/fraud-reports/{id}/resolve")
    public ResponseEntity<GeneralResponse> resolveReport(@PathVariable Long id) {
        return buildResponse("Reporte resuelto", HttpStatus.OK, fraudReportService.resolveReport(id));
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build()
                );
    }
}
