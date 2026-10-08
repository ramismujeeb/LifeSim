package com.lifesim.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class YearlySummaryDto {
    private Integer age;
    private BigDecimal netWorth;
    private BigDecimal savingsBalance;
    private BigDecimal investmentBalance;
    private BigDecimal yearlyIncome;
    private BigDecimal yearlyExpenses;
}