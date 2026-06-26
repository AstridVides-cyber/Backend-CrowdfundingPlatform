package com.example.crowdfundingplatform.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull
    private Long pledgeId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String currency;
}
