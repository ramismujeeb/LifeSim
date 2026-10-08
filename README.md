# LifeSim - Financial What-If Simulator

LifeSim is a comprehensive financial life simulation and "What If?" platform. Unlike simple budgeting tools or static calculators, LifeSim employs a dynamic timeline simulation engine. It allows users to create a baseline financial profile and instantly see the compound effects of hypothetical life scenarios on their net worth projection up to retirement age.

## Why this Project Exists
This project was built to empower users to answer complex financial questions with interactive data. Life is unpredictable; LifeSim exists to model that unpredictability by stacking multiple financial events (like job loss, major purchases, or loans) and visually graphing how they alter long-term wealth trajectory compared to a standard baseline.

## Key Features
* **Baseline Financial Projections**: Calculate monthly and yearly cash flows, savings balances, and investment growth up to a target retirement age.
* **Interactive Scenario Builder**: Model hypothetical life events including:
  * **Job Loss**: Temporarily pause salary while maintaining living expenses.
  * **Salary Growth**: Apply instant percentage bumps to income.
  * **One-Time Expenses**: Model major cash outflows like a wedding or car purchase.
  * **Recurring Expenses**: Model new monthly liabilities like childcare or hobbies.
  * **Loans**: Calculate EMI payments and their drag on monthly cash flow.
  * **Investments**: Shift cash flow into compounding market assets.
* **Financial Goals & KPIs**: Set a target retirement nest egg and see if you are on track.
* **Monte Carlo Simulations**: Run 1,000 randomized iterations of your scenario to generate probabilistic outcomes (Worst 10%, Median, Best 10%).
* **Scenario Comparison**: Visually compare your modified timeline against your original baseline via interactive Recharts.

## Architecture & Technology Stack
LifeSim uses a resilient, decoupled full-stack architecture:

* **Backend**: Java 21, Spring Boot 3, Spring Security (JWT), Spring Data JPA
* **Database**: PostgreSQL 16
* **Frontend**: React 18, TypeScript, Vite, Tailwind CSS v4, Recharts, Lucide Icons
* **Routing/Deployment**: Nginx (Reverse Proxy for Frontend and API), Docker Compose

### Backend Structure
The backend is a RESTful Spring Boot application. It handles authentication, profile storage, and the heavy lifting of the financial calculations. 
The core of the application is the `SimulationEngine.java`, which processes `FinancialProfile` inputs and `ScenarioChange` events through a month-by-month loop, applying formulas for inflation, salary growth, compounding investment returns, and EMI liability distributions.

### Frontend Structure
The frontend is a Vite-powered React Single Page Application (SPA). It uses a global `AuthContext` to manage JWT sessions (stored in localStorage). Axios is configured with request interceptors for the authorization header and response interceptors to handle graceful 401 logouts. State is passed to responsive visual dashboards and forms styled with Tailwind CSS.

## Getting Started (Docker - Recommended)

The easiest way to run LifeSim is via Docker Compose, which seamlessly spins up PostgreSQL, the Spring Boot Backend, and the Nginx/React Frontend.

### Prerequisites
* Docker and Docker Compose

### Startup
1. Clone the repository.
2. Run `docker compose up -d --build`.
3. Wait approximately 30-60 seconds for the backend to compile and the database to initialize.

### Accessing the Application
* **Frontend UI**: [http://localhost](http://localhost)
* **Backend API**: Proxied securely behind `/api/` on the frontend (e.g., `http://localhost/api/auth/login`)
* **Swagger API Docs**: [http://localhost/swagger-ui/index.html](http://localhost/swagger-ui/index.html)

To stop the application, run `docker compose down`.

## Local Development Setup

If you wish to develop without Docker Compose:

### 1. Database
* Install PostgreSQL.
* Create a database named `lifesim`.
* Set up a user/password (defaults expected: `lifesim` / `lifesim_example_password`).

### 2. Backend
* Ensure Java 21 is installed.
* Navigate to the `backend` directory.
* Run tests: `./mvnw test` (Uses H2 in-memory database).
* Run application: `./mvnw spring-boot:run`

### 3. Frontend
* Ensure Node.js (v20+) is installed.
* Navigate to the `frontend` directory.
* Install dependencies: `npm install`
* Run development server: `npm run dev`

### Environment Variables
You can configure the application using environment variables. See the provided `.env.example` in the root directory for available properties such as `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`. 

## Testing and QA
Extensive end-to-end testing and scenario math verification have been performed.
* To run backend unit and integration tests: `cd backend && ./mvnw test`
* Please see the `docs/TEST_SCENARIOS.md` file for a complete guide on how to manually reproduce and verify the simulation math.
* See `docs/QA_REPORT.md` for the final quality assurance sign-off.

## Disclaimer / Limitations
* **Not Financial Advice**: This tool is an educational simulation platform. The projections rely on constant-rate assumptions and historical averages, which are inherently unpredictable.
* **Taxes**: The engine currently operates on net/post-tax income inputs. Complex progressive tax bracket simulation is not currently implemented.
