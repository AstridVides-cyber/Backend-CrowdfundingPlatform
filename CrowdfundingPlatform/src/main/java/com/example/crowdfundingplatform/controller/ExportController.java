package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<byte[]> exportCampaigns(@PathVariable String format) {
        byte[] data = exportService.exportCampaigns(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=campaigns." + format.toLowerCase())
                .contentType(MediaType.parseMediaType("text/csv")) // O el tipo correspondiente
                .body(data);
    }
}