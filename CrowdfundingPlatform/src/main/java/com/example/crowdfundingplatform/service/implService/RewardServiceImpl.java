package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.CreateRewardRequest;
import com.example.crowdfundingplatform.domain.dto.response.RewardDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.Reward;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.exception.UnauthorizedException;
import com.example.crowdfundingplatform.mapper.RewardMapper;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.repository.RewardRepository;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final RewardMapper rewardMapper;

    @Override
    public RewardDetailResponse createReward(CreateRewardRequest request, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        Campaign campaign = getCampaignById(request.getCampaignId());
        validateOwnership(campaign, creator);
        return rewardMapper.toResponse(rewardRepository.save(rewardMapper.toEntity(request, campaign)));
    }

    @Override
    public List<RewardDetailResponse> getRewardsByCampaign(Long campaignId) {
        return rewardRepository.findByCampaignId(campaignId).stream()
                .map(rewardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RewardDetailResponse getRewardById(Long id) {
        return rewardMapper.toResponse(getRewardEntity(id));
    }

    @Override
    public RewardDetailResponse updateReward(Long id, CreateRewardRequest request, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        Reward reward = getRewardEntity(id);
        validateOwnership(reward.getCampaign(), creator);
        rewardMapper.updateEntity(reward, request);
        return rewardMapper.toResponse(rewardRepository.save(reward));
    }

    @Override
    public void deleteReward(Long id, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        Reward reward = getRewardEntity(id);
        validateOwnership(reward.getCampaign(), creator);
        rewardRepository.delete(reward);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));
    }

    private Campaign getCampaignById(Long campaignId) {
        return campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la campaña con el ID: " + campaignId));
    }

    private Reward getRewardEntity(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la recompensa con el ID: " + id));
    }

    private void validateOwnership(Campaign campaign, User creator) {
        if (!campaign.getCreator().getId().equals(creator.getId())) {
            throw new UnauthorizedException("No puedes modificar recompensas de una campaña que no es tuya");
        }
    }
}