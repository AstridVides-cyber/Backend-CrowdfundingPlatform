package com.example.crowdfundingplatform.repository;

import com.example.crowdfundingplatform.domain.entity.Pledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, Long> {

    List<Pledge> findByCampaignId(Long campaignId);
    List<Pledge> findBySponsorId(Long sponsorId);
    List<Pledge> findByCampaignIdAndChargedFalse(Long campaignId);
}
