package com.lifesim.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_profiles")
@Getter
@Setter
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer currentAge;
    private Integer retirementAge;

    private BigDecimal monthlySalary;
    private BigDecimal currentSavings;

    private BigDecimal monthlyRent;
    private BigDecimal monthlyLivingExpenses;
    private BigDecimal monthlyInvestment;

    private BigDecimal annualSalaryGrowthPercent;
    private BigDecimal annualInflationPercent;
    private BigDecimal annualInvestmentReturnPercent;

    private BigDecimal goalTargetAmount;
    private Integer goalTargetAge;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
