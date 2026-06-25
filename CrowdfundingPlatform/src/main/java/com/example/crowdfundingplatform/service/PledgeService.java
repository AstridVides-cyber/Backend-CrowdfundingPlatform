package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreatePledgeRequest;
import com.example.crowdfundingplatform.domain.dto.response.PledgeDetailResponse;

import java.util.List;

public interface PledgeService {
    PledgeDetailResponse createPledge(CreatePledgeRequest request, String sponsorEmail);
    List<PledgeDetailResponse> getMyPledges(String sponsorEmail);
    List<PledgeDetailResponse> getPledgesByCampaign(Long campaignId, String requesterEmail);
    PledgeDetailResponse refundPledge(Long id);
}