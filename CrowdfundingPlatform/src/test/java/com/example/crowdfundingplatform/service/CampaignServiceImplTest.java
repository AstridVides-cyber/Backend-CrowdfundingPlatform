package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
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
    @InjectMocks private CampaignServiceImpl campaignService;

    @Test
    void getCampaignById_ShouldThrowException_WhenNotFound() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> campaignService.getCampaignById(1L));
    }

    @Test
    void approveCampaign_ShouldSetStatusToActive_WhenPending() {
        // Arrange
        User creaor = User.builder().id(1L).name("Test Creator").build();

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.PENDING)
                .creator(creator) // <--- ESTO ES LO QUE FALTABA
                .build();

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        // Act
        var result = campaignService.approveCampaign(1L);

        // Assert
        assertEquals(CampaignStatus.ACTIVE, result.getStatus());
        verify(campaignRepository, times(1)).save(campaign);
    }

    @Test
    void approveCampaign_ShouldThrowException_WhenNotPending() {
        // Arrange
        Campaign campaign = Campaign.builder().id(1L).status(CampaignStatus.ACTIVE).build();
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> campaignService.approveCampaign(1L));
    }
}