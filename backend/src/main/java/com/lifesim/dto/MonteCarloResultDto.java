package com.lifesim.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonteCarloResultDto {
    private BigDecimal probabilityOfSuccess; // 0 to 100
    private BigDecimal medianNetWorth;
    private BigDecimal averageNetWorth;
    private BigDecimal worst10PercentNetWorth;
    private BigDecimal best10PercentNetWorth;
}
