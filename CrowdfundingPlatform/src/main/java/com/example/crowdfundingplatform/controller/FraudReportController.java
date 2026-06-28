package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreateFraudReportRequest;
import com.example.crowdfundingplatform.domain.dto.response.FraudReportDetailResponse;
import com.example.crowdfundingplatform.security.JwtAuth;
import com.example.crowdfundingplatform.service.FraudReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud-reports")
@RequiredArgsConstructor
public class FraudReportController {

    private final FraudReportService fraudReportService;

    // POST - Cualquier usuario autenticado (SPONSOR o CREATOR) reporta una campaña sospechosa
    @PostMapping
    public ResponseEntity<FraudReportDetailResponse> createReport(
            @Valid @RequestBody CreateFraudReportRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        FraudReportDetailResponse response =
                fraudReportService.createReport(request, principal.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET - El admin lista los reportes sin resolver (moderación). Usa findByResolvedFalse del repo.
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FraudReportDetailResponse>> getUnresolvedReports() {
        return ResponseEntity.ok(fraudReportService.getUnresolvedReports());
    }

    // PATCH - El admin marca un reporte como resuelto
    @PatchMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudReportDetailResponse> resolveReport(@PathVariable Long id) {
        return ResponseEntity.ok(fraudReportService.resolveReport(id));
    }
}