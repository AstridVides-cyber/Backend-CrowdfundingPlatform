package com.example.crowdfundingplatform.mapper;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.User;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CampaignMapper {

    public Campaign toEntity(CreateCampaignRequest request, User creator) {
        return Campaign.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .goal(request.getGoal())
                .goalType(request.getGoalType())
                .deadline(request.getDeadline())
                .category(request.getCategory())
                .location(request.getLocation())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .creator(creator)
                .build();
    }

    public CampaignDetailResponse toResponse(Campaign campaign) {
        return CampaignDetailResponse.builder()
                .id(campaign.getId())
                .title(campaign.getTitle())
                .description(campaign.getDescription())
                .goal(campaign.getGoal())
                .goalType(campaign.getGoalType())
                .deadline(campaign.getDeadline())
                .status(campaign.getStatus())
                .category(campaign.getCategory())
                .location(campaign.getLocation())
                .isFeatured(campaign.getIsFeatured())
                .createdAt(campaign.getCreatedAt())
                .creatorId(campaign.getCreator().getId())
                .creatorName(campaign.getCreator().getName())
                .build();
    }

    public List<CampaignDetailResponse> toListResponse(List<Campaign> campaigns) {
        return campaigns.stream().map(this::toResponse).toList();
    }

}