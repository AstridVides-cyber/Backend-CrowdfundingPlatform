package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.CreateRewardRequest;
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

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    // POST - El creador agrega una recompensa a su campaña (campaignId viaja en el body)
    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<RewardDetailResponse> createReward(
            @Valid @RequestBody CreateRewardRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        // principal.getUsername() devuelve el email del usuario autenticado
        RewardDetailResponse response = rewardService.createReward(request, principal.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET - Lista las recompensas de una campaña: /api/rewards?campaignId=1
    @GetMapping
    public ResponseEntity<List<RewardDetailResponse>> getRewardsByCampaign(@RequestParam Long campaignId) {
        return ResponseEntity.ok(rewardService.getRewardsByCampaign(campaignId));
    }

    // GET - Obtiene una recompensa por su id
    @GetMapping("/{id}")
    public ResponseEntity<RewardDetailResponse> getRewardById(@PathVariable Long id) {
        return ResponseEntity.ok(rewardService.getRewardById(id));
    }

    // PUT - El creador edita una recompensa propia (la validación de "dueño" va en el service)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<RewardDetailResponse> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody CreateRewardRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        return ResponseEntity.ok(rewardService.updateReward(id, request, principal.getUsername()));
    }

    // DELETE - El creador elimina una recompensa propia
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<Void> deleteReward(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtAuth principal) {
        rewardService.deleteReward(id, principal.getUsername());
        // 204: operación exitosa sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}