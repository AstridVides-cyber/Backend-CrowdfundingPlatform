package com.example.crowdfundingplatform.domain.dto.request;

import com.example.crowdfundingplatform.domain.enums.GoalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCampaignRequest {

    @NotBlank(message = "El título de la campaña no puede estar vacío")
    @Size(max = 150, message = "El título no debe exceder los 150 caracteres")
    private String title;

    @NotBlank(message = "La descripción de la campaña es obligatoria")
    private String description;

    @NotNull(message = "La meta de recaudación es obligatoria")
    @Positive(message = "El monto meta debe ser mayor a cero")
    private BigDecimal goal;

    @NotNull(message = "El tipo de meta es obligatorio")
    private GoalType goalType;

    @NotNull(message = "La fecha límite es obligatoria")
    @Future(message = "La fecha límite debe ser futura")
    private LocalDateTime deadline;

    private String category;
    private String location;
    private Boolean isFeatured;
}