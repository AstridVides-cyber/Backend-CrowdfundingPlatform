package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.CreateCampaignRequest;
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
import java.util.stream.Collectors;

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
        return campaignRepository.findAll()
                .stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByStatus(CampaignStatus status) {
        return campaignRepository.findByStatus(status)
                .stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByCategory(String category) {
        return campaignRepository.findByCategory(category)
                .stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByLocation(String location) {
        return campaignRepository.findByLocation(location)
                .stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getFeaturedCampaigns() {
        return campaignRepository.findByIsFeaturedTrue()
                .stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignDetailResponse> getCampaignsByCreator(Long creatorId) {
        return campaignRepository.findByCreatorId(creatorId)
                .stream()
                .map(campaignMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CampaignDetailResponse approveCampaign(Long id) {
        Campaign campaign = findById(id);
        if (campaign.getStatus() != CampaignStatus.PENDING) {
            throw new BadRequestException("Solo se pueden aprobar campañas en estado PENDING");
        }
        campaign.setStatus(CampaignStatus.ACTIVE);
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse rejectCampaign(Long id) {
        Campaign campaign = findById(id);
        if (campaign.getStatus() != CampaignStatus.PENDING) {
            throw new BadRequestException("Solo se pueden rechazar campañas en estado PENDING");
        }
        campaign.setStatus(CampaignStatus.CANCELLED);
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignDetailResponse updateCampaign(Long id, CreateCampaignRequest request, String creatorEmail) {
        Campaign campaign = findById(id);
        campaignMapper.updateEntity(campaign, request);
        return campaignMapper.toResponse(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {
        campaignRepository.delete(findById(id));
    }

    private Campaign findById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaña no encontrada con id: " + id));
    }
}