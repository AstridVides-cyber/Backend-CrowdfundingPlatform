package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.mapper.CampaignMapper;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.service.implService.CampaignServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @Mock private CampaignRepository campaignRepository;
    @Mock private CampaignMapper campaignMapper;
    @InjectMocks private CampaignServiceImpl campaignService;

    @Test
    void getCampaignById_ShouldThrowException_WhenNotFound() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> campaignService.getCampaignById(1L));
    }

    @Test
    void approveCampaign_ShouldSetStatusToActive_WhenPending() {
        User creator = User.builder().id(1L).name("Test Creator").build();
        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.PENDING)
                .creator(creator)
                .build();

        CampaignDetailResponse response = CampaignDetailResponse.builder()
                .id(1L)
                .status(CampaignStatus.ACTIVE)
                .build();

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);
        when(campaignMapper.toResponse(campaign)).thenReturn(response);

        var result = campaignService.approveCampaign(1L);

        assertEquals(CampaignStatus.ACTIVE, result.getStatus());
        verify(campaignRepository, times(1)).save(campaign);
        verify(campaignMapper, times(1)).toResponse(campaign);
    }

    @Test
    void approveCampaign_ShouldThrowException_WhenNotPending() {
        Campaign campaign = Campaign.builder().id(1L).status(CampaignStatus.ACTIVE).build();
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        assertThrows(BadRequestException.class, () -> campaignService.approveCampaign(1L));
    }
}