package com.example.crowdfundingplatform.domain.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private String paymentIntentId;
    private String clientSecret;
    private String status;
    private String currency;
    private Long amount;
}
