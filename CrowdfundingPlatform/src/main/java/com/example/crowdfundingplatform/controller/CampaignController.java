package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.dto.request.UpdateCampaign;
import com.example.crowdfundingplatform.domain.dto.response.GeneralResponse;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> createCampaign(@Valid @RequestBody CreateCampaignRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return buildResponse("Creada", HttpStatus.CREATED, campaignService.createCampaign(request, userDetails.getUsername()));
    }

    @GetMapping("/allCampaigns")
    public ResponseEntity<GeneralResponse> getAllCampaigns() {
        return buildResponse("OK", HttpStatus.OK, campaignService.getAllCampaigns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getCampaignById(@PathVariable Long id) {
        return buildResponse("OK", HttpStatus.OK, campaignService.getCampaignById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<GeneralResponse> getCampaignsByStatus(@PathVariable CampaignStatus status) {
        return buildResponse("OK", HttpStatus.OK, campaignService.getCampaignsByStatus(status));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<GeneralResponse> getCampaignsByCategory(@PathVariable String category) {
        return buildResponse("OK", HttpStatus.OK, campaignService.getCampaignsByCategory(category));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<GeneralResponse> getCampaignsByLocation(@PathVariable String location) {
        return buildResponse("OK", HttpStatus.OK, campaignService.getCampaignsByLocation(location));
    }

    @GetMapping("/featured")
    public ResponseEntity<GeneralResponse> getFeaturedCampaigns() {
        return buildResponse("OK", HttpStatus.OK, campaignService.getFeaturedCampaigns());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> updateCampaign(@PathVariable Long id, @Valid @RequestBody UpdateCampaign request, @AuthenticationPrincipal UserDetails userDetails) {
        return buildResponse("Actualizada", HttpStatus.OK, campaignService.updateCampaign(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<GeneralResponse> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return buildResponse("Eliminada", HttpStatus.OK, null);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> approveCampaign(@PathVariable Long id) {
        return buildResponse("Aprobada", HttpStatus.OK, campaignService.approveCampaign(id));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> rejectCampaign(@PathVariable Long id) {
        return buildResponse("Rechazada", HttpStatus.OK, campaignService.rejectCampaign(id));
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status).body(GeneralResponse.builder()
                .uri(uri).message(message).status(status.value())
                .time(LocalDateTime.now()).data(data).build());
    }
}