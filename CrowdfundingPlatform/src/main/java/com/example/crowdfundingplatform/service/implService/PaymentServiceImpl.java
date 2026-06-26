package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.PaymentRequest;
import com.example.crowdfundingplatform.domain.dto.response.PaymentResponse;
import com.example.crowdfundingplatform.domain.entity.Pledge;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.ResourceNotFoundException;
import com.example.crowdfundingplatform.repository.PledgeRepository;
import com.example.crowdfundingplatform.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    private final PledgeRepository pledgeRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        try {

            long amountInCents = request.getAmount()
                    .multiply(new java.math.BigDecimal("100"))
                    .longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setDescription("Pledge #" + request.getPledgeId() + " - Crowdfunding Platform")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return PaymentResponse.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .status(paymentIntent.getStatus())
                    .currency(paymentIntent.getCurrency())
                    .amount(paymentIntent.getAmount())
                    .build();

        } catch (StripeException e) {
            throw new BadRequestException("Error al crear el pago: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse confirmPayment(String paymentIntentId, Long pledgeId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            Pledge pledge = pledgeRepository.findById(pledgeId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Pledge no encontrado con id: " + pledgeId));

            pledge.setCharged(true);
            pledgeRepository.save(pledge);

            return PaymentResponse.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .status(paymentIntent.getStatus())
                    .currency(paymentIntent.getCurrency())
                    .amount(paymentIntent.getAmount())
                    .build();

        } catch (StripeException e) {
            throw new BadRequestException("Error al confirmar el pago: " + e.getMessage());
        }
    }
}