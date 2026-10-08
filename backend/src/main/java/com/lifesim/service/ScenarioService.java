package com.lifesim.service;
import com.lifesim.dto.ScenarioDto;
import com.lifesim.dto.ScenarioChangeDto;
import com.lifesim.model.Scenario;
import com.lifesim.model.ScenarioChange;
import com.lifesim.model.User;
import com.lifesim.repository.ScenarioRepository;
import com.lifesim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScenarioService {
    @Autowired ScenarioRepository scenarioRepository;
    @Autowired UserRepository userRepository;

    public List<ScenarioDto> getScenarios(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return scenarioRepository.findByUserId(user.getId()).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public ScenarioDto getScenario(Long id) {
        return mapToDto(scenarioRepository.findById(id).orElseThrow());
    }

    public ScenarioDto createScenario(String email, ScenarioDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Scenario scenario = new Scenario();
        scenario.setUser(user);
        scenario.setName(dto.getName());
        scenario.setChanges(dto.getChanges().stream().map(c -> mapToEntity(c, scenario)).collect(Collectors.toList()));
        return mapToDto(scenarioRepository.save(scenario));
    }
    
    public ScenarioDto updateScenario(Long id, ScenarioDto dto) {
        Scenario scenario = scenarioRepository.findById(id).orElseThrow();
        scenario.setName(dto.getName());
        scenario.getChanges().clear();
        if (dto.getChanges() != null) {
            scenario.getChanges().addAll(dto.getChanges().stream().map(c -> mapToEntity(c, scenario)).collect(Collectors.toList()));
        }
        return mapToDto(scenarioRepository.save(scenario));
    }

    public void deleteScenario(Long id) {
        scenarioRepository.deleteById(id);
    }

    private ScenarioDto mapToDto(Scenario s) {
        ScenarioDto dto = new ScenarioDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        if (s.getChanges() != null) {
            dto.setChanges(s.getChanges().stream().map(this::mapChangeToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    private ScenarioChangeDto mapChangeToDto(ScenarioChange c) {
        ScenarioChangeDto dto = new ScenarioChangeDto();
        dto.setId(c.getId());
        dto.setType(c.getType());
        dto.setStartAge(c.getStartAge());
        dto.setStartMonth(c.getStartMonth());
        dto.setDurationMonths(c.getDurationMonths());
        dto.setAmount(c.getAmount());
        dto.setPercentage(c.getPercentage());
        dto.setInterestRate(c.getInterestRate());
        return dto;
    }

    private ScenarioChange mapToEntity(ScenarioChangeDto dto, Scenario scenario) {
        ScenarioChange c = new ScenarioChange();
        c.setScenario(scenario);
        c.setType(dto.getType());
        c.setStartAge(dto.getStartAge());
        c.setStartMonth(dto.getStartMonth());
        c.setDurationMonths(dto.getDurationMonths());
        c.setAmount(dto.getAmount());
        c.setPercentage(dto.getPercentage());
        c.setInterestRate(dto.getInterestRate());
        return c;
    }
}