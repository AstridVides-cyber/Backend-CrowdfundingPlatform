package com.example.crowdfundingplatform.mapper;

import com.example.crowdfundingplatform.domain.dto.request.CreateRewardRequest;
import com.example.crowdfundingplatform.domain.dto.response.RewardDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.Reward;
import org.springframework.stereotype.Component;

@Component
public class RewardMapper {

    public Reward toEntity(CreateRewardRequest request, Campaign campaign) {
        return Reward.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .minimumAmount(request.getMinimumAmount())
                .quantity(request.getQuantity())
                .campaign(campaign)
                .build();
    }

    public void updateEntity(Reward reward, CreateRewardRequest request) {
        reward.setTitle(request.getTitle());
        reward.setDescription(request.getDescription());
        reward.setMinimumAmount(request.getMinimumAmount());
        reward.setQuantity(request.getQuantity());
    }

    public RewardDetailResponse toResponse(Reward reward) {
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
