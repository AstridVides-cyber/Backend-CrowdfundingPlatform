package com.example.crowdfundingplatform.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFraudReportRequest {

    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(max = 5000, message = "El motivo no debe exceder los 5000 caracteres")
    private String reason;

    @NotNull(message = "El id del reportero no puede ser nulo")
    private Long reporterId;

    @NotNull(message = "El id de la campaña no puede ser nulo")
    private Long campaignId;
}