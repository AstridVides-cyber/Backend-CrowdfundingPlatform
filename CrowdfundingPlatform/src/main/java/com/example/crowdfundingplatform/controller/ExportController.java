package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/{format}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
    public ResponseEntity<String> exportCampaigns(@PathVariable String format) {
        return ResponseEntity.ok(exportService.exportCampaigns(format));
    }
}