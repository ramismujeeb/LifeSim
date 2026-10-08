package com.lifesim.repository;
import com.lifesim.model.ScenarioChange;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ScenarioChangeRepository extends JpaRepository<ScenarioChange, Long> {
}