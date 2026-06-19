package com.example.crowdfundingplatform.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePledgeRequest {

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal amount;

    @NotNull(message = "El id del sponsor no puede ser nulo")
    private Long sponsorId;

    @NotNull(message = "El id de la campaña no puede ser nulo")
    private Long campaignId;

    private Long rewardId;
}