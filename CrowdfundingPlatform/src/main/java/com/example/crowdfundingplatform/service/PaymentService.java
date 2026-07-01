package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.PaymentRequest;
import com.example.crowdfundingplatform.domain.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPaymentIntent(PaymentRequest request);
    PaymentResponse confirmPayment(String paymentIntentId, Long pledgeId);
}
