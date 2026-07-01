package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.CreatePledgeRequest;
import com.example.crowdfundingplatform.domain.dto.response.PledgeDetailResponse;
import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.entity.Pledge;
import com.example.crowdfundingplatform.domain.entity.Reward;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.domain.enums.Role;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.exception.UnauthorizedException;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.repository.PledgeRepository;
import com.example.crowdfundingplatform.repository.RewardRepository;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.service.PledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.crowdfundingplatform.mapper.PledgeMapper;

@Service
@RequiredArgsConstructor
public class PledgeServiceImpl implements PledgeService {

    private final PledgeRepository pledgeRepository;
    private final CampaignRepository campaignRepository;
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final PledgeMapper pledgeMapper;

    @Override
    public PledgeDetailResponse createPledge(CreatePledgeRequest request, String sponsorEmail) {
        User sponsor = getUserByEmail(sponsorEmail);
        Campaign campaign = getCampaignById(request.getCampaignId());

        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new BadRequestException("La campaña no está activa para recibir promesas");
        }

        Reward reward = null;
        if (request.getRewardId() != null) {
            reward = rewardRepository.findById(request.getRewardId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró la recompensa con el ID: " + request.getRewardId()));

            if (!reward.getCampaign().getId().equals(campaign.getId())) {
                throw new BadRequestException("La recompensa no pertenece a esta campaña");
            }
            if (request.getAmount().compareTo(reward.getMinimumAmount()) < 0) {
                throw new BadRequestException("El monto no alcanza el mínimo de la recompensa elegida");
            }
        }
        Pledge pledge = pledgeMapper.toEntity(request, sponsor, campaign, reward);
        return pledgeMapper.toResponse(pledgeRepository.save(pledge));
    }

    @Override
    public List<PledgeDetailResponse> getMyPledges(String sponsorEmail) {
        User sponsor = getUserByEmail(sponsorEmail);
        return pledgeMapper.toListResponse(pledgeRepository.findBySponsorId(sponsor.getId()));
    }

    @Override
    public List<PledgeDetailResponse> getPledgesByCampaign(Long campaignId, String requesterEmail) {
        User requester = getUserByEmail(requesterEmail);
        Campaign campaign = getCampaignById(campaignId);

        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isOwner = campaign.getCreator().getId().equals(requester.getId());
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("No tienes permiso para ver las promesas de esta campaña");
        }

        return pledgeMapper.toListResponse(pledgeRepository.findByCampaignId(campaignId));
    }

    @Override
    public PledgeDetailResponse refundPledge(Long id) {
        Pledge pledge = pledgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la promesa con el ID: " + id));

        if (Boolean.TRUE.equals(pledge.getRefunded())) {
            throw new BadRequestException("Esta promesa ya fue reembolsada");
        }

        pledge.setRefunded(true);
        return pledgeMapper.toResponse(pledgeRepository.save(pledge));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));
    }

    private Campaign getCampaignById(Long campaignId) {
        return campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la campaña con el ID: " + campaignId));
    }

}
