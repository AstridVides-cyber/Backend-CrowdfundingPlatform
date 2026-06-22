package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.CampaignDTO;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException; // Importación necesaria
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        // 1. Convertimos de DTO a Entidad
        Campaign campaign = new Campaign();
        campaign.setTitle(campaignDTO.getTitle());
        campaign.setDescription(campaignDTO.getDescription());
        campaign.setGoal(campaignDTO.getGoal());

        // El resto de campos (status, createdAt) se manejan por @PrePersist en la entidad
        Campaign savedCampaign = campaignRepository.save(campaign);

        // 3. Devolvemos el DTO
        return mapToDTO(savedCampaign);
    }

    public List<CampaignDTO> getAllCampaigns() {
        List<Campaign> campaigns = campaignRepository.findAll();
        return campaigns.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CampaignDTO getCampaignById(Long id) {
        // Aquí aplicamos el cambio: si no existe, lanza la excepción personalizada
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la campaña con el ID: " + id));
        return mapToDTO(campaign);
    }

    // Método auxiliar
    private CampaignDTO mapToDTO(Campaign campaign) {
        return CampaignDTO.builder()
                .id(campaign.getId())
                .title(campaign.getTitle())
                .description(campaign.getDescription())
                .goal(campaign.getGoal())
                .currentAmount(BigDecimal.ZERO)
                .campaignStatus(campaign.getStatus() != null ? campaign.getStatus().name() : null)
                .build();
    }
}