package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
import com.example.crowdfundingplatform.domain.dto.request.UpdateCampaign;
import com.example.crowdfundingplatform.domain.dto.response.CampaignDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.mapper.CampaignMapper;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignMapper campaignMapper;

    @Override
    public CampaignDetailResponse createCampaign(CreateCampaignRequest request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Creator no encontrado"));
        Campaign campaign = campaignMapper.toEntity(request, creator);
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse getCampaignById(Long id) {
        return campaignMapper.toResponse(findById(id));
    }

    @Override
    public List<CampaignDetailResponse> getAllCampaigns() {
        return campaignMapper.toListResponse(campaignRepository.findAll());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByStatus(CampaignStatus status) {
        return campaignMapper.toListResponse(campaignRepository.findByStatus(status));
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByCategory(String category) {
        return campaignMapper.toListResponse(campaignRepository.findByCategory(category));
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByLocation(String location) {
        return campaignMapper.toListResponse(campaignRepository.findByLocation(location));
    }

    @Override
    public List<CampaignDetailResponse> getFeaturedCampaigns() {
        return campaignMapper.toListResponse(campaignRepository.findByIsFeaturedTrue());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByCreator(Long creatorId) {
        return campaignMapper.toListResponse(campaignRepository.findByCreatorId(creatorId));
    }

    @Override
    public CampaignDetailResponse approveCampaign(Long id) {
        Campaign campaign = findById(id);
        if (campaign.getStatus() != CampaignStatus.PENDING) throw new BadRequestException("Solo PENDING");
        campaign.setStatus(CampaignStatus.ACTIVE);
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse rejectCampaign(Long id) {
        Campaign campaign = findById(id);
        if (campaign.getStatus() != CampaignStatus.PENDING) throw new BadRequestException("Solo PENDING");
        campaign.setStatus(CampaignStatus.CANCELLED);
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse updateCampaign(Long id, UpdateCampaign request) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaña no encontrada: " + id));
        if (request.getTitle() != null){
            campaign.setTitle(request.getTitle());
        }
        if (request.getDescription() != null){
            campaign.setDescription(request.getDescription());
        }
        if (request.getCategory() != null){
            campaign.setCategory(request.getCategory());
        }
        if (request.getLocation() != null){
            campaign.setLocation(request.getLocation());
        }
        if (request.getGoal() != null){
            campaign.setGoal(request.getGoal());
        }
        if (request.getGoalType() != null){
            campaign.setGoalType(request.getGoalType());
        }
        if (request.getDeadline() != null){
            campaign.setDeadline(request.getDeadline());
        }
        if (request.getIsFeatured() != null){
            campaign.setIsFeatured(request.getIsFeatured());
        }
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {
        campaignRepository.delete(findById(id));
    }

    private Campaign findById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaña no encontrada: " + id));
    }
}