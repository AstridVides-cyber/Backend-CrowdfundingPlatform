package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.PaymentRequest;
import com.example.crowdfundingplatform.domain.dto.response.PaymentResponse;
import com.example.crowdfundingplatform.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-intent")
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<PaymentResponse> createPaymentIntent(
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentIntent(request));
    }

    @PostMapping("/confirm/{pledgeId}")
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @PathVariable Long pledgeId,
            @RequestParam String paymentIntentId) {
        return ResponseEntity.ok(paymentService.confirmPayment(paymentIntentId, pledgeId));
    }
}
