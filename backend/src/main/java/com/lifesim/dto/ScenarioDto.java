package com.lifesim.dto;
import lombok.Data;
import java.util.List;
@Data
public class ScenarioDto {
    private Long id;
    private String name;
    private List<ScenarioChangeDto> changes;
}