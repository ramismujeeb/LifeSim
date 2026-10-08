package com.lifesim.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class ScenarioChangeDto {
    private Long id;
    private String type;
    private Integer startAge;
    private Integer startMonth;
    private Integer durationMonths;
    private BigDecimal amount;
    private BigDecimal percentage;
    private BigDecimal interestRate;
}