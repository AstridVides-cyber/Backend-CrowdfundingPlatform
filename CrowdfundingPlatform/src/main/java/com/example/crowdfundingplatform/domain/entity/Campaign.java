package com.example.crowdfundingplatform.domain.entity;

import com.example.crowdfundingplatform.domain.enums.CampaignStatus;
import com.example.crowdfundingplatform.domain.enums.GoalType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal goal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalType goalType;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status;

    private String category;
    private String location;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Creador de la campaña (CREATOR)
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // Recompensas disponibles
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<Reward> rewards;

    // Pledges recibidos
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<Pledge> pledges;

    // Reportes de fraude
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<FraudReport> fraudReports;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = CampaignStatus.PENDING;
    }
}