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

    // POST - El creador agrega una recompensa a su campaña (campaignId viaja en el body)
    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> createReward(
            @Valid @RequestBody CreateRewardRequest request,
            @AuthenticationPrincipal JwtAuth principal) {
        // principal.getUsername() devuelve el email del usuario autenticado
        RewardDetailResponse response = rewardService.createReward(request, principal.getUsername());
        return buildResponse("Recompensa creada exitosamente", HttpStatus.CREATED, response);
    }

    // GET - Lista las recompensas de una campaña: /api/rewards?campaignId=1
    @GetMapping
    public ResponseEntity<GeneralResponse> getRewardsByCampaign(@RequestParam Long campaignId) {
        return buildResponse("Recompensas obtenidas exitosamente",
                HttpStatus.OK,
                rewardService.getRewardsByCampaign(campaignId)
        );
    }

    // GET - Obtiene una recompensa por su id
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getRewardById(@PathVariable Long id) {
        return buildResponse("Recompensa obtenida exitosamente",
                HttpStatus.OK,
                rewardService.getRewardById(id)
        );
    }

    // PUT - El creador edita una recompensa propia (la validación de "dueño" va en el service)
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

    // DELETE - El creador elimina una recompensa propia
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GeneralResponse> deleteReward(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtAuth principal) {
        rewardService.deleteReward(id, principal.getUsername());
        // 204: operación exitosa sin cuerpo de respuesta
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