package com.lifesim.service;
import com.lifesim.dto.SimulationResultDto;
import com.lifesim.engine.SimulationEngine;
import com.lifesim.model.FinancialProfile;
import com.lifesim.model.Scenario;
import com.lifesim.repository.FinancialProfileRepository;
import com.lifesim.repository.ScenarioRepository;
import com.lifesim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class SimulationService {
    @Autowired SimulationEngine engine;
    @Autowired FinancialProfileRepository profileRepo;
    @Autowired ScenarioRepository scenarioRepo;
    @Autowired UserRepository userRepo;

    public SimulationResultDto runBaseline(String email) {
        FinancialProfile profile = getProfile(email);
        return engine.simulate(profile, new ArrayList<>());
    }

    public SimulationResultDto runScenario(String email, Long scenarioId) {
        FinancialProfile profile = getProfile(email);
        Scenario scenario = scenarioRepo.findById(scenarioId).orElseThrow();
        return engine.simulate(profile, scenario.getChanges());
    }

    public com.lifesim.dto.MonteCarloResultDto runMonteCarloBaseline(String email) {
        FinancialProfile profile = getProfile(email);
        return engine.runMonteCarlo(profile, new ArrayList<>(), 1000);
    }

    public com.lifesim.dto.MonteCarloResultDto runMonteCarloScenario(String email, Long scenarioId) {
        FinancialProfile profile = getProfile(email);
        Scenario scenario = scenarioRepo.findById(scenarioId).orElseThrow();
        return engine.runMonteCarlo(profile, scenario.getChanges(), 1000);
    }

    private FinancialProfile getProfile(String email) {
        Long userId = userRepo.findByEmail(email).orElseThrow().getId();
        return profileRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}