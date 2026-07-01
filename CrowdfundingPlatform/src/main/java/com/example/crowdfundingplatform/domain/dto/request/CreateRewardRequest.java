package com.example.crowdfundingplatform.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRewardRequest {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 150, message = "El título no debe exceder los 150 caracteres")
    private String title;

    private String description;

    @NotNull(message = "El monto mínimo no puede ser nulo")
    @Positive(message = "El monto mínimo debe ser mayor a cero")
    private BigDecimal minimumAmount;

    private Integer quantity;

    @NotNull(message = "El id de la campaña no puede ser nulo")
    private Long campaignId;
}