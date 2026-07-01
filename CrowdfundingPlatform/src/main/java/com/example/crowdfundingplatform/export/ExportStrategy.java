package com.example.crowdfundingplatform.export;

import com.example.crowdfundingplatform.domain.entity.Campaign;
import java.util.List;

public interface ExportStrategy {
    byte[] export(List<Campaign> campaigns);
    String getFormat();
}