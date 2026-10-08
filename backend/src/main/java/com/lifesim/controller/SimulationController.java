package com.lifesim.controller;
import com.lifesim.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/simulations")
public class SimulationController {
    @Autowired SimulationService simulationService;

    @GetMapping("/baseline")
    public ResponseEntity<?> runBaseline(Authentication authentication) {
        try {
            return ResponseEntity.ok(simulationService.runBaseline(authentication.getName()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/scenario/{id}/run")
    public ResponseEntity<?> runScenario(Authentication authentication, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(simulationService.runScenario(authentication.getName(), id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/baseline/monte-carlo")
    public ResponseEntity<?> runMonteCarloBaseline(Authentication authentication) {
        try {
            return ResponseEntity.ok(simulationService.runMonteCarloBaseline(authentication.getName()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/scenario/{id}/monte-carlo")
    public ResponseEntity<?> runMonteCarloScenario(Authentication authentication, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(simulationService.runMonteCarloScenario(authentication.getName(), id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}