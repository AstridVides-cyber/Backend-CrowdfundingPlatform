package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import java.util.List;

public interface CampaignService {
    CampaignDetailResponse createCampaign(CreateCampaignRequest request, String creatorEmail);
    CampaignDetailResponse getCampaignById(Long id);
    List<CampaignDetailResponse> getAllCampaigns();
    List<CampaignDetailResponse> getCampaignsByStatus(CampaignStatus status);
    List<CampaignDetailResponse> getCampaignsByCategory(String category);
    List<CampaignDetailResponse> getCampaignsByLocation(String location);
    List<CampaignDetailResponse> getFeaturedCampaigns();
    List<CampaignDetailResponse> getCampaignsByCreator(Long creatorId);
    CampaignDetailResponse approveCampaign(Long id);
    CampaignDetailResponse rejectCampaign(Long id);
    CampaignDetailResponse updateCampaign(Long id, CreateCampaignRequest request, String creatorEmail);
    void deleteCampaign(Long id);
}