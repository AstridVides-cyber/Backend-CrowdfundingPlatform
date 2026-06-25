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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PledgeServiceImpl implements PledgeService {

    private final PledgeRepository pledgeRepository;
    private final CampaignRepository campaignRepository;
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;

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

        Pledge pledge = Pledge.builder()
                .amount(request.getAmount())
                .charged(false)
                .refunded(false)
                .sponsor(sponsor)
                .campaign(campaign)
                .reward(reward)
                .build();

        return mapToResponse(pledgeRepository.save(pledge));
    }

    @Override
    public List<PledgeDetailResponse> getMyPledges(String sponsorEmail) {
        User sponsor = getUserByEmail(sponsorEmail);
        return pledgeRepository.findBySponsorId(sponsor.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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

        return pledgeRepository.findByCampaignId(campaignId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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
        return mapToResponse(pledgeRepository.save(pledge));
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

    private PledgeDetailResponse mapToResponse(Pledge pledge) {
        return PledgeDetailResponse.builder()
                .id(pledge.getId())
                .amount(pledge.getAmount())
                .charged(pledge.getCharged())
                .refunded(pledge.getRefunded())
                .createdAt(pledge.getCreatedAt())
                .sponsorId(pledge.getSponsor().getId())
                .sponsorName(pledge.getSponsor().getName())
                .campaignId(pledge.getCampaign().getId())
                .campaignTitle(pledge.getCampaign().getTitle())
                .rewardId(pledge.getReward() != null ? pledge.getReward().getId() : null)
                .rewardTitle(pledge.getReward() != null ? pledge.getReward().getTitle() : null)
                .build();
    }
}
