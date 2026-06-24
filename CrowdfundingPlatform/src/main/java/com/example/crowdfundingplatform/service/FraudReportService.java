package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreateFraudReportRequest;
import com.example.crowdfundingplatform.domain.dto.response.FraudReportDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.FraudReport;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.exception.UnauthorizedException;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.repository.FraudReportRepository;
import com.example.crowdfundingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FraudReportService {

    private final FraudReportRepository fraudReportRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    // Cualquier usuario autenticado reporta una campaña sospechosa
    public FraudReportDetailResponse createReport(CreateFraudReportRequest request, String reporterEmail) {
        User reporter = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));

        Campaign campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la campaña con el ID: " + request.getCampaignId()));

        FraudReport report = FraudReport.builder()
                .reason(request.getReason())
                .resolved(false)
                .reporter(reporter)
                .campaign(campaign)
                .build();

        return mapToResponse(fraudReportRepository.save(report));
    }

    // El admin lista los reportes sin resolver (moderación)
    public List<FraudReportDetailResponse> getUnresolvedReports() {
        return fraudReportRepository.findByResolvedFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // El admin marca un reporte como resuelto
    public FraudReportDetailResponse resolveReport(Long id) {
        FraudReport report = fraudReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte con el ID: " + id));

        report.setResolved(true);
        return mapToResponse(fraudReportRepository.save(report));
    }

    private FraudReportDetailResponse mapToResponse(FraudReport report) {
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
}