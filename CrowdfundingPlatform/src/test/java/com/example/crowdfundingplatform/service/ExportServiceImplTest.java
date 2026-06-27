package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.export.ExportStrategy;
import com.example.crowdfundingplatform.repository.CampaignRepository;
import com.example.crowdfundingplatform.service.implService.ExportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceImplTest {

    @Mock private CampaignRepository campaignRepository;
    @Mock private ExportStrategy csvExporter;
    @InjectMocks private ExportServiceImpl exportService;

    @Test
    void exportCampaigns_ShouldReturnData_WhenFormatIsValid() {
        // Arrange
        String format = "CSV";
        when(csvExporter.getFormat()).thenReturn("CSV");

        // Creamos la lista de estrategias (la inyectamos manualmente al mock)
        exportService = new ExportServiceImpl(campaignRepository, List.of(csvExporter));

        when(csvExporter.export(anyList())).thenReturn("data".getBytes());

        // Act
        byte[] result = exportService.exportCampaigns(format);

        // Assert
        assertNotNull(result);
        assertEquals("data", new String(result));
    }

    @Test
    void exportCampaigns_ShouldThrowException_WhenFormatIsInvalid() {
        exportService = new ExportServiceImpl(campaignRepository, List.of());
        assertThrows(IllegalArgumentException.class, () -> exportService.exportCampaigns("INVALID"));
    }
}