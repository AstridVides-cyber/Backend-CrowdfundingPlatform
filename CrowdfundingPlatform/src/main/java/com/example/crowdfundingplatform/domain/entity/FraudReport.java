package com.example.crowdfundingplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private Boolean resolved = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Usuario que reportó
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    // Campaña reportada
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
