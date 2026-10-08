package com.lifesim.engine;
import com.lifesim.dto.MonteCarloResultDto;
import com.lifesim.dto.SimulationResultDto;
import com.lifesim.dto.YearlySummaryDto;
import com.lifesim.model.FinancialProfile;
import com.lifesim.model.ScenarioChange;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class SimulationEngine {
    
    private final Random random = new Random();

    public SimulationResultDto simulate(FinancialProfile profile, List<ScenarioChange> changes) {
        return runSingleSimulation(profile, changes, false);
    }

    public MonteCarloResultDto runMonteCarlo(FinancialProfile profile, List<ScenarioChange> changes, int simulations) {
        List<BigDecimal> finalNetWorths = new ArrayList<>();
        int successes = 0;
        BigDecimal target = profile.getGoalTargetAmount();
        
        for (int i = 0; i < simulations; i++) {
            SimulationResultDto result = runSingleSimulation(profile, changes, true);
            if (!result.getYearlySummaries().isEmpty()) {
                YearlySummaryDto lastYear = result.getYearlySummaries().get(result.getYearlySummaries().size() - 1);
                finalNetWorths.add(lastYear.getNetWorth());
                
                if (target != null && lastYear.getNetWorth().compareTo(target) >= 0) {
                    successes++;
                }
            }
        }
        
        if (finalNetWorths.isEmpty()) {
            return new MonteCarloResultDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        
        Collections.sort(finalNetWorths);
        
        BigDecimal prob = target != null ? BigDecimal.valueOf((successes * 100.0) / simulations) : BigDecimal.valueOf(100);
        BigDecimal worst10 = finalNetWorths.get(Math.max(0, (int)(simulations * 0.1) - 1));
        BigDecimal median = finalNetWorths.get(simulations / 2);
        BigDecimal best10 = finalNetWorths.get(Math.min(simulations - 1, (int)(simulations * 0.9)));
        
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal nw : finalNetWorths) sum = sum.add(nw);
        BigDecimal average = sum.divide(BigDecimal.valueOf(simulations), 2, RoundingMode.HALF_UP);
        
        return new MonteCarloResultDto(prob, median, average, worst10, best10);
    }

    private SimulationResultDto runSingleSimulation(FinancialProfile profile, List<ScenarioChange> changes, boolean isMonteCarlo) {
        List<YearlySummaryDto> summaries = new ArrayList<>();
        
        int currentAge = profile.getCurrentAge() != null ? profile.getCurrentAge() : 0;
        int retirementAge = profile.getRetirementAge() != null ? profile.getRetirementAge() : 65;
        
        BigDecimal currentSavings = profile.getCurrentSavings() != null ? profile.getCurrentSavings() : BigDecimal.ZERO;
        BigDecimal investmentBalance = BigDecimal.ZERO;
        
        BigDecimal monthlySalary = profile.getMonthlySalary() != null ? profile.getMonthlySalary() : BigDecimal.ZERO;
        BigDecimal monthlyRent = profile.getMonthlyRent() != null ? profile.getMonthlyRent() : BigDecimal.ZERO;
        BigDecimal monthlyLiving = profile.getMonthlyLivingExpenses() != null ? profile.getMonthlyLivingExpenses() : BigDecimal.ZERO;
        BigDecimal monthlyInvestment = profile.getMonthlyInvestment() != null ? profile.getMonthlyInvestment() : BigDecimal.ZERO;
        
        BigDecimal baseSalaryGrowth = profile.getAnnualSalaryGrowthPercent() != null ? profile.getAnnualSalaryGrowthPercent() : BigDecimal.ZERO;
        BigDecimal baseInflation = profile.getAnnualInflationPercent() != null ? profile.getAnnualInflationPercent() : BigDecimal.ZERO;
        BigDecimal baseInvestmentReturn = profile.getAnnualInvestmentReturnPercent() != null ? profile.getAnnualInvestmentReturnPercent() : BigDecimal.ZERO;
        
        for (int age = currentAge; age <= retirementAge; age++) {
            BigDecimal yearlyIncome = BigDecimal.ZERO;
            BigDecimal yearlyExpenses = BigDecimal.ZERO;
            
            // Randomize annual rates if Monte Carlo
            BigDecimal annualSalaryGrowth = baseSalaryGrowth;
            BigDecimal annualInflation = baseInflation;
            BigDecimal annualInvestmentReturn = baseInvestmentReturn;
            
            if (isMonteCarlo) {
                // vary roughly by +/- 4%
                annualSalaryGrowth = varyRate(baseSalaryGrowth, 4.0);
                annualInflation = varyRate(baseInflation, 2.0);
                annualInvestmentReturn = varyRate(baseInvestmentReturn, 5.0);
            }
            
            for (int month = 1; month <= 12; month++) {
                BigDecimal currentMonthSalary = monthlySalary;
                BigDecimal currentMonthRent = monthlyRent;
                BigDecimal currentMonthLiving = monthlyLiving;
                BigDecimal currentMonthInvestment = monthlyInvestment;
                
                if (changes != null) {
                    for (ScenarioChange change : changes) {
                        int startAge = change.getStartAge() != null ? change.getStartAge() : 0;
                        int startMonth = change.getStartMonth() != null ? change.getStartMonth() : 1;
                        int duration = change.getDurationMonths() != null ? change.getDurationMonths() : 0;
                        
                        int currentTotalMonths = (age * 12) + month;
                        int startTotalMonths = (startAge * 12) + startMonth;
                        
                        if (currentTotalMonths >= startTotalMonths && (duration == 0 || currentTotalMonths < startTotalMonths + duration)) {
                            if ("JOB_LOSS".equals(change.getType())) {
                                currentMonthSalary = BigDecimal.ZERO;
                                currentMonthInvestment = BigDecimal.ZERO;
                            } else if ("ONE_TIME_EXPENSE".equals(change.getType()) && currentTotalMonths == startTotalMonths) {
                                BigDecimal expense = change.getAmount() != null ? change.getAmount() : BigDecimal.ZERO;
                                if (currentSavings.compareTo(expense) >= 0) {
                                    currentSavings = currentSavings.subtract(expense);
                                } else {
                                    BigDecimal remainder = expense.subtract(currentSavings);
                                    currentSavings = BigDecimal.ZERO;
                                    investmentBalance = investmentBalance.subtract(remainder);
                                }
                                yearlyExpenses = yearlyExpenses.add(expense);
                            } else if ("SALARY_GROWTH".equals(change.getType())) {
                                if (currentTotalMonths == startTotalMonths && change.getPercentage() != null) {
                                    BigDecimal growth = BigDecimal.ONE.add(change.getPercentage().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
                                    monthlySalary = monthlySalary.multiply(growth);
                                    currentMonthSalary = monthlySalary;
                                }
                            } else if ("RECURRING_EXPENSE".equals(change.getType())) {
                                BigDecimal expense = change.getAmount() != null ? change.getAmount() : BigDecimal.ZERO;
                                currentMonthLiving = currentMonthLiving.add(expense);
                            } else if ("LOAN".equals(change.getType())) {
                                BigDecimal principal = change.getAmount() != null ? change.getAmount() : BigDecimal.ZERO;
                                BigDecimal rate = change.getInterestRate() != null ? change.getInterestRate() : BigDecimal.ZERO;
                                if (rate.compareTo(BigDecimal.ZERO) > 0 && duration > 0) {
                                    BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
                                    BigDecimal onePlusRPowerN = BigDecimal.ONE.add(monthlyRate).pow(duration);
                                    BigDecimal emi = principal.multiply(monthlyRate).multiply(onePlusRPowerN)
                                        .divide(onePlusRPowerN.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP);
                                    currentMonthLiving = currentMonthLiving.add(emi);
                                } else if (duration > 0) {
                                    currentMonthLiving = currentMonthLiving.add(principal.divide(BigDecimal.valueOf(duration), 10, RoundingMode.HALF_UP));
                                }
                            } else if ("INVESTMENT".equals(change.getType())) {
                                BigDecimal invAmount = change.getAmount() != null ? change.getAmount() : BigDecimal.ZERO;
                                currentMonthInvestment = currentMonthInvestment.add(invAmount);
                            }
                        }
                    }
                }
                
                yearlyIncome = yearlyIncome.add(currentMonthSalary);
                BigDecimal totalMonthlyExpenses = currentMonthRent.add(currentMonthLiving);
                yearlyExpenses = yearlyExpenses.add(totalMonthlyExpenses);
                
                currentSavings = currentSavings.add(currentMonthSalary).subtract(totalMonthlyExpenses).subtract(currentMonthInvestment);
                investmentBalance = investmentBalance.add(currentMonthInvestment);
                
                if (investmentBalance.compareTo(BigDecimal.ZERO) > 0 && annualInvestmentReturn.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal monthlyReturn = annualInvestmentReturn.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
                    investmentBalance = investmentBalance.add(investmentBalance.multiply(monthlyReturn));
                }
            }
            
            BigDecimal netWorth = currentSavings.add(investmentBalance);
            summaries.add(new YearlySummaryDto(age, netWorth, currentSavings, investmentBalance, yearlyIncome, yearlyExpenses));
            
            if (annualSalaryGrowth.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal multiplier = BigDecimal.ONE.add(annualSalaryGrowth.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
                monthlySalary = monthlySalary.multiply(multiplier);
            }
            if (annualInflation.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal multiplier = BigDecimal.ONE.add(annualInflation.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP));
                monthlyRent = monthlyRent.multiply(multiplier);
                monthlyLiving = monthlyLiving.multiply(multiplier);
            }
        }
        
        return new SimulationResultDto(summaries);
    }
    
    private BigDecimal varyRate(BigDecimal base, double maxVariance) {
        double variance = (random.nextDouble() * maxVariance * 2) - maxVariance; 
        return base.add(BigDecimal.valueOf(variance));
    }
}