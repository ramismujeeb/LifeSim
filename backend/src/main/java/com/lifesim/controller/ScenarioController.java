package com.lifesim.controller;
import com.lifesim.dto.ScenarioDto;
import com.lifesim.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {
    @Autowired ScenarioService scenarioService;

    @GetMapping
    public ResponseEntity<?> getScenarios(Authentication authentication) {
        return ResponseEntity.ok(scenarioService.getScenarios(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScenario(@PathVariable Long id) {
        return ResponseEntity.ok(scenarioService.getScenario(id));
    }

    @PostMapping
    public ResponseEntity<?> createScenario(Authentication authentication, @RequestBody ScenarioDto dto) {
        return ResponseEntity.ok(scenarioService.createScenario(authentication.getName(), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScenario(@PathVariable Long id, @RequestBody ScenarioDto dto) {
        return ResponseEntity.ok(scenarioService.updateScenario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScenario(@PathVariable Long id) {
        scenarioService.deleteScenario(id);
        return ResponseEntity.ok().build();
    }
}