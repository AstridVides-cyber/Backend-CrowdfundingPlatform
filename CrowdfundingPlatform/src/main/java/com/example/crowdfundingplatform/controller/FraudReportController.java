package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreateFraudReportRequest;
import com.example.crowdfundingplatform.domain.dto.response.FraudReportDetailResponse;
import com.example.crowdfundingplatform.domain.dto.response.GeneralResponse;
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
import java.time.LocalDateTime;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        return buildResponse("Reporte creado exitosamente", HttpStatus.CREATED, response);
    }

    // GET - El admin lista los reportes sin resolver (moderación). Usa findByResolvedFalse del repo.
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> getUnresolvedReports() {
        return buildResponse("Reportes sin resolver obtenidos", HttpStatus.OK, fraudReportService.getUnresolvedReports());
    }

    // PATCH - El admin marca un reporte como resuelto
    @PatchMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> resolveReport(@PathVariable Long id) {
        return buildResponse("Reporte resuelto", HttpStatus.OK, fraudReportService.resolveReport(id));
    }

    public ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
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