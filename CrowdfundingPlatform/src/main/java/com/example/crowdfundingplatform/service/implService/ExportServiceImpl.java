package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.export.ExportStrategy;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final CampaignRepository campaignRepository;
    private final List<ExportStrategy> exporters;

    @Override
    public String exportCampaigns(String format) {
        return exporters.stream()
                .filter(e -> e.getFormat().equalsIgnoreCase(format))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Formato no soportado: " + format))
                .export(campaignRepository.findAll());
    }
}