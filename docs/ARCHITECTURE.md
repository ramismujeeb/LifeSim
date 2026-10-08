# Architecture

LifeSim is a modular monolith built using Spring Boot.

## Layers
1. **Controller Layer**: Handles REST HTTP requests, security, and DTO validation.
2. **Service Layer**: Handles business logic, including database transactions and calling the `SimulationEngine`.
3. **Simulation Engine**: A core deterministic engine (`com.lifesim.engine.SimulationEngine`) that calculates monthly financial state projections over time until retirement.
4. **Data Access Layer**: Spring Data JPA repositories interacting with PostgreSQL.

## Frontend
The frontend uses React and Recharts to visualize the complex output from the Simulation Engine. State is managed locally and via Context API for authentication.
