package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreateRewardRequest;
import com.example.crowdfundingplatform.domain.dto.response.GeneralResponse;
import com.example.crowdfundingplatform.domain.dto.response.RewardDetailResponse;
import com.example.crowdfundingplatform.security.JwtAuth;
import com.example.crowdfundingplatform.service.RewardService;
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
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    // POST /api/rewards - CREATOR - Crear recompensa
    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> createReward(
            @Valid @RequestBody CreateRewardRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        RewardDetailResponse response = rewardService.createReward(request, principal.getUsername());
        return buildResponse("Recompensa creada exitosamente", HttpStatus.CREATED, response);
    }

    // GET /api/rewards/{id} - Autenticado - Detalle de recompensa
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getRewardById(@PathVariable Long id) {
        return buildResponse("Recompensa obtenida exitosamente",
                HttpStatus.OK,
                rewardService.getRewardById(id)
        );
    }

    // GET /api/rewards/campaign/{id} - Autenticado - Recompensas de campaña
    @GetMapping("/campaign/{id}")
    public ResponseEntity<GeneralResponse> getRewardsByCampaign(@PathVariable("id") Long campaignId) {
        return buildResponse("Recompensas obtenidas exitosamente",
                HttpStatus.OK,
                rewardService.getRewardsByCampaign(campaignId)
        );
    }

    // PUT /api/rewards/{id} - CREATOR - Actualizar recompensa
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody CreateRewardRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        return buildResponse("Recompensa actualizada exitosamente",
                HttpStatus.OK,
                rewardService.updateReward(id, request, principal.getUsername())
        );
    }

    // DELETE /api/rewards/{id} - CREATOR - Eliminar recompensa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> deleteReward(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtAuth principal) {
        rewardService.deleteReward(id, principal.getUsername());
        return buildResponse("Recompensa eliminada exitosamente",
                HttpStatus.NO_CONTENT,
                null
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