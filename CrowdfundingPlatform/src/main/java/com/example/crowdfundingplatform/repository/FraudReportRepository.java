package com.example.crowdfundingplatform.repository;

import com.example.crowdfundingplatform.domain.entity.FraudReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraudReportRepository extends JpaRepository<FraudReport, Long> {

    List<FraudReport> findByCampaignId(Long campaignId);
    List<FraudReport> findByResolvedFalse();
}
