package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreateRewardRequest;
import com.example.crowdfundingplatform.domain.dto.response.RewardDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.Reward;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.exception.UnauthorizedException;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.repository.RewardRepository;
import com.example.crowdfundingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    // Crear una recompensa para una campaña propia del creador
    public RewardDetailResponse createReward(CreateRewardRequest request, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        Campaign campaign = getCampaignById(request.getCampaignId());

        // Regla: solo el dueño de la campaña puede agregarle recompensas
        validateOwnership(campaign, creator);

        Reward reward = Reward.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .minimumAmount(request.getMinimumAmount())
                .quantity(request.getQuantity())
                .campaign(campaign)
                .build();

        return mapToResponse(rewardRepository.save(reward));
    }

    // Listar las recompensas de una campaña
    public List<RewardDetailResponse> getRewardsByCampaign(Long campaignId) {
        return rewardRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Obtener una recompensa por su id
    public RewardDetailResponse getRewardById(Long id) {
        return mapToResponse(getRewardEntity(id));
    }

    // Actualizar una recompensa propia
    public RewardDetailResponse updateReward(Long id, CreateRewardRequest request, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        Reward reward = getRewardEntity(id);

        validateOwnership(reward.getCampaign(), creator);

        reward.setTitle(request.getTitle());
        reward.setDescription(request.getDescription());
        reward.setMinimumAmount(request.getMinimumAmount());
        reward.setQuantity(request.getQuantity());

        return mapToResponse(rewardRepository.save(reward));
    }

    // Eliminar una recompensa propia
    public void deleteReward(Long id, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        Reward reward = getRewardEntity(id);

        validateOwnership(reward.getCampaign(), creator);

        rewardRepository.delete(reward);
    }

    // ---------- Métodos auxiliares ----------

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));
    }

    private Campaign getCampaignById(Long campaignId) {
        return campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la campaña con el ID: " + campaignId));
    }

    private Reward getRewardEntity(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la recompensa con el ID: " + id));
    }

    private void validateOwnership(Campaign campaign, User creator) {
        if (!campaign.getCreator().getId().equals(creator.getId())) {
            throw new UnauthorizedException("No puedes modificar recompensas de una campaña que no es tuya");
        }
    }

    private RewardDetailResponse mapToResponse(Reward reward) {
        return RewardDetailResponse.builder()
                .id(reward.getId())
                .title(reward.getTitle())
                .description(reward.getDescription())
                .minimumAmount(reward.getMinimumAmount())
                .quantity(reward.getQuantity())
                .campaignId(reward.getCampaign().getId())
                .campaignTitle(reward.getCampaign().getTitle())
                .build();
    }
}