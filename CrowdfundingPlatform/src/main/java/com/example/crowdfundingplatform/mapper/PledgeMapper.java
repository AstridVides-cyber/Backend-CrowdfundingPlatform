package com.example.crowdfundingplatform.mapper;

import com.example.crowdfundingplatform.domain.dto.request.CreatePledgeRequest;
import com.example.crowdfundingplatform.domain.dto.response.PledgeDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.Pledge;
import com.example.crowdfundingplatform.domain.entity.Reward;
import com.example.crowdfundingplatform.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PledgeMapper {

    public Pledge toEntity(CreatePledgeRequest request, User sponsor, Campaign campaign, Reward reward) {
        return Pledge.builder()
                .amount(request.getAmount())
                .charged(false)
                .refunded(false)
                .sponsor(sponsor)
                .campaign(campaign)
                .reward(reward)
                .build();
    }

    public PledgeDetailResponse toResponse(Pledge pledge) {
        return PledgeDetailResponse.builder()
                .id(pledge.getId())
                .amount(pledge.getAmount())
                .charged(pledge.getCharged())
                .refunded(pledge.getRefunded())
                .createdAt(pledge.getCreatedAt())
                .sponsorId(pledge.getSponsor().getId())
                .sponsorName(pledge.getSponsor().getName())
                .campaignId(pledge.getCampaign().getId())
                .campaignTitle(pledge.getCampaign().getTitle())
                .rewardId(pledge.getReward() != null ? pledge.getReward().getId() : null)
                .rewardTitle(pledge.getReward() != null ? pledge.getReward().getTitle() : null)
                .build();
    }

    public List<PledgeDetailResponse> toListResponse(List<Pledge> pledges) {
        return pledges.stream().map(this::toResponse).toList();
    }
}