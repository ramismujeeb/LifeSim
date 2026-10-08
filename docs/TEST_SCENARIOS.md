# LifeSim Manual Test Scenarios

This document contains mathematically verified test scenarios to validate the LifeSim simulation engine's core cash-flow, compounding, and scenario logic.

## Standard Baseline Profile
For Tests 1-3 and 5-9, ensure your **Financial Profile** is configured exactly as follows to keep the baseline math clean:

* **Current Age:** `30`
* **Retirement Age:** `32`
* **Monthly Salary:** `$10,000`
* **Current Savings:** `$10,000`
* **Monthly Rent:** `$2,000`
* **Monthly Living Expenses:** `$1,000`
* **Monthly Investment:** `$2,000`
* **Annual Salary Growth:** `0%`
* **Annual Inflation:** `0%`
* **Annual Investment Return:** `0%`

*Baseline Net Worth Expectations:*
* Age 30: **$94,000**
* Age 31: **$178,000**
* Age 32: **$262,000**

---

### Test 1: Baseline Cash-Flow Check
**Math Breakdown:**
* Income: `+$10,000`
* Expenses (Rent + Living): `-$3,000`
* Investment Contribution: `-$2,000`
* **Net Cash to Savings:** `$5,000 / month`

**Year 1 (Age 30):**
* Savings = Initial $10k + (12 * $5k) = $70,000
* Investments = (12 * $2k) = $24,000
* **Net Worth:** $70,000 + $24,000 = **$94,000**

---

### Test 2: The One-Time Expense
* **Event:** `ONE_TIME_EXPENSE` | Age: `31` | Amount: `$50,000`
* **Math (Age 31):**
    * Starts with Age 30 Net Worth: $94,000.
    * $50,000 expense instantly drains cash.
    * Year 2 baseline generation: +$84,000 ($60k savings, $24k investments).
    * **Net Worth at Age 31:** $94,000 - $50,000 + $84,000 = **$128,000**.

---

### Test 3: The 6-Month Job Loss
* **Event:** `JOB_LOSS` | Age: `31` | Duration: `6` months
* **Math (Age 31):**
    * Months 1-6: $0 income. $3k/mo expenses. Savings drain = -$18,000.
    * Months 7-12: Normal income resumes. Savings gain = +$30,000. Inv gain = +$12,000.
    * Net wealth added for the year: -$18k + $30k + $12k = +$24,000.
    * **Net Worth at Age 31:** $94k (from Age 30) + $24k = **$118,000**.

---

### Test 4: The "10% Economy" (Inflation & Growth)
* **Profile Change:** Set Salary Growth to `10%` and Inflation to `10%`.
* **Math:**
    * **Age 30:** Multipliers apply at year-end. **Net Worth = $94,000**.
    * **Age 31:** Salary = $11,000. Rent = $2,200. Living = $1,100. Inv = $2,000.
      * Net cash to savings = $5,700/mo.
      * Total wealth added = (12 * $5,700) + $24k = $92,400.
      * **Net Worth at Age 31:** $94,000 + $92,400 = **$186,400**.
    * **Age 32:** Salary = $12,100. Rent = $2,420. Living = $1,210.
      * Net cash to savings = $6,470/mo.
      * Total wealth added = (12 * $6,470) + $24k = $101,640.
      * **Net Worth at Age 32:** $186,400 + $101,640 = **$288,040**.

---

### Test 5: The "0% Car Loan" (Testing EMI)
* **Profile:** Revert to 0% growth baseline.
* **Event:** `LOAN` | Age: `31` | Amount: `$12,000` | Duration: `12` | Interest: `0%`
* **Math (Age 31):**
    * 0% EMI for $12k over 12 months = $1,000 / month.
    * Monthly cash to savings drops from $5k to $4k.
    * Wealth added = (12 * $4,000) + (12 * $2,000) = $72,000.
    * **Net Worth at Age 31:** $94,000 + $72,000 = **$166,000**.

---

### Test 6: The "Startup Promotion" (Salary Jump)
* **Event:** `SALARY_GROWTH` | Age: `31` | Percentage: `50%`
* **Math (Age 31):**
    * Salary immediately jumps from $10k to $15k.
    * New cash to savings = 15k - 2k - 1k - 2k = $10,000 / month.
    * Wealth added = (12 * $10,000) + $24,000 = $144,000.
    * **Net Worth at Age 31:** $94,000 + $144,000 = **$238,000**.

---

### Test 7: The "New Baby" (Recurring Expense)
* **Event:** `RECURRING_EXPENSE` | Age: `31` | Amount: `$2,000` | Duration: `12`
* **Math:**
    * **Age 31:** Living expenses increase by $2k to $3k total. Net cash to savings drops to $3,000/mo.
      * Wealth added = (12 * $3,000) + $24k = $60,000.
      * **Net Worth at Age 31:** $94,000 + $60,000 = **$154,000**.
    * **Age 32:** Duration ends. Cash flow returns to baseline.
      * Wealth added = $84,000.
      * **Net Worth at Age 32:** $154,000 + $84,000 = **$238,000**.

---

### Test 8: The "Aggressive Saver" (Investment Shift Identity)
* **Event:** `INVESTMENT` | Age: `31` | Amount: `$3,000` | Duration: `12`
* **Math (Age 31):**
    * With a 0% Investment Return, adding $3k to investments simply moves it from Savings to Investments.
    * Cash to savings drops from $5k/mo to $2k/mo.
    * Investment contribution rises from $2k/mo to $5k/mo.
    * Wealth added = (12 * $2k) + (12 * $5k) = $84,000 (Identical to baseline!).
    * **Net Worth at Age 31:** **$178,000** (Scenario line exactly covers Baseline line).
    * *(Setting Investment Return to 12% in Profile will cause the scenario line to break out above the baseline).*

---

### Test 9: "Sabbatical & Travel" (Multi-Event Engine Check)
* **Event 1:** `JOB_LOSS` | Age: `31` | Duration: `6`
* **Event 2:** `ONE_TIME_EXPENSE` | Age: `31` | Amount: `$30,000`
* **Math (Age 31):**
    * *Day 1:* $30k RV expense drains cash.
    * *Months 1-6:* Unemployed. Income = $0. Expenses = $3k/mo. Drain = -$18k.
    * *Months 7-12:* Employed. Income resumes. Gain = +$30k savings, +$12k investments.
    * *End of Year 2 Savings:* $70k (start) - $30k (RV) - $18k (unemployed) + $30k (employed) = $52,000.
    * *End of Year 2 Investments:* $24k (start) + $12k (employed) = $36,000.
    * **Net Worth at Age 31:** $52,000 + $36,000 = **$88,000**.
* **Math (Age 32):**
    * Back to baseline cash flow (+$84k/yr).
    * **Net Worth at Age 32:** $88,000 + $84,000 = **$172,000**.
