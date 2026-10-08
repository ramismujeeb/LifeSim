package com.lifesim.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;
@Data
@AllArgsConstructor
public class SimulationResultDto {
    private List<YearlySummaryDto> yearlySummaries;
}