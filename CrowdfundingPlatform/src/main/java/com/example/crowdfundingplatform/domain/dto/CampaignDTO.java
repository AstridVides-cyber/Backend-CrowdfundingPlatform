package com.example.crowdfundingplatform.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDTO {

    private Long id;

    @NotBlank(message = "El título de la campaña no puede estar vacío")
    @Size(max = 150, message = "El título no debe exceder los 150 caracteres")
    private String title;

    @NotBlank(message = "La descripción de la campaña es obligatoria")
    private String description;

    @NotNull(message = "La meta de recaudación es obligatoria")
    @Positive(message = "El monto meta debe ser mayor a cero")
    private BigDecimal goal;


    private BigDecimal currentAmount;

    // Dependiendo de como lo manejemos , podemos  devolver el estado como String
    private String campaignStatus;
}