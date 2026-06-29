package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreatePledgeRequest;
import com.example.crowdfundingplatform.domain.dto.response.GeneralResponse;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pledges")
@RequiredArgsConstructor
public class PledgeController {

    private final PledgeService pledgeService;

    // POST - El patrocinador promete una donación.
    // Regla "todo o nada": el service debe crearla con charged=false (no se cobra aún).
    @PostMapping("/create")
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<GeneralResponse> createPledge(
            @Valid @RequestBody CreatePledgeRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        PledgeDetailResponse response = pledgeService.createPledge(request, principal.getUsername());
        return buildResponse("Promesa creada exitosamente", HttpStatus.CREATED, response);
    }

    // GET - El patrocinador ve sus propias promesas (campañas que sigue / historial)
    @GetMapping("/my")
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<GeneralResponse> getMyPledges(
            @AuthenticationPrincipal JwtAuth principal) {
        return buildResponse("Promesas obtenidas exitosamente",
                HttpStatus.OK,
                pledgeService.getMyPledges(principal.getUsername())
        );
    }

    // GET - Promesas de una campaña: /api/pledges?campaignId=1
    // Solo el creador dueño de la campaña o un admin. El service valida la pertenencia.
    @GetMapping
    @PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
    public ResponseEntity<GeneralResponse> getPledgesByCampaign(
            @RequestParam Long campaignId,
            @AuthenticationPrincipal JwtAuth principal) {
        return buildResponse("Promesas de la campaña obtenidas exitosamente",
                HttpStatus.OK,
                pledgeService.getPledgesByCampaign(campaignId, principal.getUsername())
        );
    }

    // PATCH - Reembolso de una promesa (campaña fallida / decisión de admin)
    @PatchMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> refundPledge(@PathVariable Long id) {
        return buildResponse("Promesa reembolsada exitosamente",
                HttpStatus.OK,
                pledgeService.refundPledge(id)
        );
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