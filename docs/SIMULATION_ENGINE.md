# Simulation Engine

The core logic of LifeSim lives in `SimulationEngine.java`.

## Logic Flow
1. Load the user's `FinancialProfile` (Current Age, Savings, Income, Expenses, Retirement Age).
2. Load requested `ScenarioChange`s (e.g. Job Loss at month X for Y months).
3. Loop from `Current Age` to `Retirement Age`.
4. Loop through 12 months per year.
5. Apply specific scenario rules per month (e.g. override salary to 0 if Job Loss is active).
6. Update Net Worth = (Current Savings + Investments).
7. Generate `YearlySummaryDto` containing end-of-year balances.
8. Apply annual inflation and salary growth.
9. Return the accumulated list of `YearlySummaryDto`s.

This guarantees a deterministic output that is fully testable.
