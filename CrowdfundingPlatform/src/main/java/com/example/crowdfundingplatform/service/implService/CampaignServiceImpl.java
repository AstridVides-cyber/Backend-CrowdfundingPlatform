package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    @Override
    public CampaignDetailResponse createCampaign(CreateCampaignRequest request) {
        Campaign campaign = Campaign.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .goal(request.getGoal())
                .goalType(request.getGoalType())
                .deadline(request.getDeadline())
                .category(request.getCategory())
                .location(request.getLocation())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .build();

        return mapToResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse getCampaignById(Long id) {
        return mapToResponse(findById(id));
    }

    @Override
    public List<CampaignDetailResponse> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByStatus(CampaignStatus status) {
        return campaignRepository.findByStatus(status)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByCategory(String category) {
        return campaignRepository.findByCategory(category)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByLocation(String location) {
        return campaignRepository.findByLocation(location)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getFeaturedCampaigns() {
        return campaignRepository.findByIsFeaturedTrue()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByCreator(Long creatorId) {
        return campaignRepository.findByCreatorId(creatorId)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CampaignDetailResponse approveCampaign(Long id) {
        Campaign campaign = findById(id);
        if (campaign.getStatus() != CampaignStatus.PENDING) {
            throw new BadRequestException("Solo se pueden aprobar campañas en estado PENDING");
        }
        campaign.setStatus(CampaignStatus.ACTIVE);
        return mapToResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse rejectCampaign(Long id) {
        Campaign campaign = findById(id);
        if (campaign.getStatus() != CampaignStatus.PENDING) {
            throw new BadRequestException("Solo se pueden rechazar campañas en estado PENDING");
        }
        campaign.setStatus(CampaignStatus.CANCELLED);
        return mapToResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse updateCampaign(Long id, CreateCampaignRequest request) {
        Campaign campaign = findById(id);
        campaign.setTitle(request.getTitle());
        campaign.setDescription(request.getDescription());
        campaign.setGoal(request.getGoal());
        campaign.setGoalType(request.getGoalType());
        campaign.setDeadline(request.getDeadline());
        campaign.setCategory(request.getCategory());
        campaign.setLocation(request.getLocation());
        return mapToResponse(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {
        campaignRepository.delete(findById(id));
    }

    private Campaign findById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaña no encontrada con id: " + id));
    }

    private CampaignDetailResponse mapToResponse(Campaign campaign) {
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
}
