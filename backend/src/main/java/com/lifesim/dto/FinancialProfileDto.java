package com.lifesim.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class FinancialProfileDto {
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
}