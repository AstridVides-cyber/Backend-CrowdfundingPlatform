package com.example.crowdfundingplatform.repository;

import com.example.crowdfundingplatform.domain.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}