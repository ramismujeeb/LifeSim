package com.lifesim.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "scenario_changes")
@Getter
@Setter
public class ScenarioChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @Column(nullable = false)
    private String type; // JOB_LOSS, SALARY_GROWTH, ONE_TIME_EXPENSE, LOAN, INVESTMENT

    // Depending on the type, different fields are used
    private Integer startAge;
    private Integer startMonth; // relative to start age or global month
    private Integer durationMonths;

    private BigDecimal amount;
    private BigDecimal percentage;
    private BigDecimal interestRate;
}
