package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.CampaignDTO;
import com.example.crowdfundingplatform.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    // Inyección de dependencias de la capa de negocio (Servicio)
    private final CampaignService campaignService;

    //POST
    // Endpoint para crear una campaña
    @PostMapping
    public ResponseEntity<CampaignDTO> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
        CampaignDTO createdCampaign = campaignService.createCampaign(campaignDTO);
        return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
    }
    // GET
    // Endpoint para obtener todas las campañas
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    //GET
    // Endpoint para obtener una campaña por su ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable Long id) {
        CampaignDTO campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(campaign);
    }
}