package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreatePledgeRequest;
import com.example.crowdfundingplatform.domain.dto.response.PledgeDetailResponse;
import com.example.crowdfundingplatform.security.JwtAuth;
import com.example.crowdfundingplatform.service.PledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pledges")
@RequiredArgsConstructor
public class PledgeController {

    private final PledgeService pledgeService;

    // POST - El patrocinador promete una donación.
    // Regla "todo o nada": el service debe crearla con charged=false (no se cobra aún).
    @PostMapping
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<PledgeDetailResponse> createPledge(
            @Valid @RequestBody CreatePledgeRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        PledgeDetailResponse response = pledgeService.createPledge(request, principal.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET - El patrocinador ve sus propias promesas (campañas que sigue / historial)
    @GetMapping("/my")
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<List<PledgeDetailResponse>> getMyPledges(
            @AuthenticationPrincipal JwtAuth principal) {
        return ResponseEntity.ok(pledgeService.getMyPledges(principal.getUsername()));
    }

    // GET - Promesas de una campaña: /api/pledges?campaignId=1
    // Solo el creador dueño de la campaña o un admin. El service valida la pertenencia.
    @GetMapping
    @PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
    public ResponseEntity<List<PledgeDetailResponse>> getPledgesByCampaign(
            @RequestParam Long campaignId,
            @AuthenticationPrincipal JwtAuth principal) {
        return ResponseEntity.ok(pledgeService.getPledgesByCampaign(campaignId, principal.getUsername()));
    }

    // PATCH - Reembolso de una promesa (campaña fallida / decisión de admin)
    @PatchMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PledgeDetailResponse> refundPledge(@PathVariable Long id) {
        return ResponseEntity.ok(pledgeService.refundPledge(id));
    }
}