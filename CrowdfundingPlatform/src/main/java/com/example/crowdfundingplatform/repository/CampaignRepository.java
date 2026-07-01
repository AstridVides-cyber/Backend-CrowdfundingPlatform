package com.example.crowdfundingplatform.repository;

import com.example.crowdfundingplatform.domain.entity.Campaign;
import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(CampaignStatus status);
    List<Campaign> findByCategory(String category);
    List<Campaign> findByLocation(String location);
    List<Campaign> findByIsFeaturedTrue();
    List<Campaign> findByCreatorId(Long creatorId);
}