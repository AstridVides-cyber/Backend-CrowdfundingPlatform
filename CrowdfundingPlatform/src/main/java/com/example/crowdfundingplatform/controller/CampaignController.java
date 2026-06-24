package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<CampaignDetailResponse> createCampaign(
            @Valid @RequestBody CreateCampaignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(campaignService.createCampaign(request));
    }

    @GetMapping
    public ResponseEntity<List<CampaignDetailResponse>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDetailResponse> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CampaignDetailResponse>> getCampaignsByStatus(
            @PathVariable CampaignStatus status) {
        return ResponseEntity.ok(campaignService.getCampaignsByStatus(status));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CampaignDetailResponse>> getCampaignsByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(campaignService.getCampaignsByCategory(category));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<CampaignDetailResponse>> getCampaignsByLocation(
            @PathVariable String location) {
        return ResponseEntity.ok(campaignService.getCampaignsByLocation(location));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<CampaignDetailResponse>> getFeaturedCampaigns() {
        return ResponseEntity.ok(campaignService.getFeaturedCampaigns());
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<CampaignDetailResponse>> getCampaignsByCreator(
            @PathVariable Long creatorId) {
        return ResponseEntity.ok(campaignService.getCampaignsByCreator(creatorId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<CampaignDetailResponse> updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CreateCampaignRequest request) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignDetailResponse> approveCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.approveCampaign(id));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignDetailResponse> rejectCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.rejectCampaign(id));
    }
}