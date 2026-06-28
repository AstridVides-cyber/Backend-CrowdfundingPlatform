package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreateRewardRequest;
import com.example.crowdfundingplatform.domain.dto.response.RewardDetailResponse;

import java.util.List;

public interface RewardService {
    RewardDetailResponse createReward(CreateRewardRequest request, String creatorEmail);
    List<RewardDetailResponse> getRewardsByCampaign(Long campaignId);
    RewardDetailResponse getRewardById(Long id);
    RewardDetailResponse updateReward(Long id, CreateRewardRequest request, String creatorEmail);
    void deleteReward(Long id, String creatorEmail);
}