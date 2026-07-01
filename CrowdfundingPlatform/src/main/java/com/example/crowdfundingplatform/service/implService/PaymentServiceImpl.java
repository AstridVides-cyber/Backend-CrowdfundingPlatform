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

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${SK_TEST}")
    private String stripeSecretKey;

    private final PledgeRepository pledgeRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public PaymentResponse createPaymentIntent(PaymentRequest request) {
        try {
            // Calculando comision
            BigDecimal commission = request.getAmount().multiply(new BigDecimal("0.05"));
            BigDecimal netAmount = request.getAmount().subtract(commission);

            // Guardar en el pledge antes del cobro
            Pledge pledge = pledgeRepository.findById(request.getPledgeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pledge no encontrado"));
            pledge.setCommissionAmount(commission);
            pledge.setNetAmount(netAmount);
            pledgeRepository.save(pledge);

            // Stripe cobra el monto total sin comision
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

            if (pledge.getCommissionAmount() == null) {
                BigDecimal commission = pledge.getAmount().multiply(new BigDecimal("0.05"));
                BigDecimal netAmount = pledge.getAmount().subtract(commission);
                pledge.setCommissionAmount(commission);
                pledge.setNetAmount(netAmount);
            }

            pledge.setCharged(true);
            pledgeRepository.save(pledge);

            return PaymentResponse.builder()
                    .paymentIntentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .status(paymentIntent.getStatus())
                    .currency(paymentIntent.getCurrency())
                    .amount(paymentIntent.getAmount())
                    .totalAmount(pledge.getAmount())
                    .commissionAmount(pledge.getCommissionAmount())
                    .netAmount(pledge.getNetAmount())
                    .build();

        } catch (StripeException e) {
            throw new BadRequestException("Error al confirmar el pago: " + e.getMessage());
        }
    }
}