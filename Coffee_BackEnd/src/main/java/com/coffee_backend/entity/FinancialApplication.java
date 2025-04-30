package com.coffee_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.coffee_backend.enumType.ApplicationStatus;

@Entity
@Table(name = "financial_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "financial_support_id", nullable = false)
    private FinancialSupport financialSupport;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String purpose;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "admin_feedback")
    private String adminFeedback;

    @Column(name = "apply_time")
    private LocalDateTime applyTime;

    @PrePersist
    protected void onCreate() {
        applyTime = LocalDateTime.now();
    }
}
