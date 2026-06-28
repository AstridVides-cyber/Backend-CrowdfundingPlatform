package com.example.crowdfundingplatform.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Monto mínimo para obtener esta recompensa
    @Column(nullable = false)
    private BigDecimal minimumAmount;
    private Integer quantity; // Cantidad disponible de esta recompensa

    // Campaña a la que pertenece
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    // Pledges que eligieron esta recompensa
    @OneToMany(mappedBy = "reward", cascade = CascadeType.ALL)
    private List<Pledge> pledges;

}
