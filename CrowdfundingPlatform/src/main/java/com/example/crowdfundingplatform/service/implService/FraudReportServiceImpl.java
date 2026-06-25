package com.example.crowdfundingplatform.service.implService;

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
import com.example.crowdfundingplatform.service.FraudReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FraudReportServiceImpl implements FraudReportService {

    private final FraudReportRepository fraudReportRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    @Override
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

    @Override
    public List<FraudReportDetailResponse> getUnresolvedReports() {
        return fraudReportRepository.findByResolvedFalse().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FraudReportDetailResponse resolveReport(Long id) {
        FraudReport report = fraudReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el reporte con el ID: " + id));

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