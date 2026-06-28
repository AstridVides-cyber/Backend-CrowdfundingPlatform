package com.example.crowdfundingplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pledges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    // Si el pago fue cobrado o no
    @Column(nullable = false)
    private Boolean charged = false;

    // Si fue reembolsado
    @Column(nullable = false)
    private Boolean refunded = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Sponsor que hizo el pledge
    @ManyToOne
    @JoinColumn(name = "sponsor_id", nullable = false)
    private User sponsor;

    // Campaña a la que pertenece
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    // Recompensa elegida (opcional)
    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
