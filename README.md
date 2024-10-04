# MOCDO

## jMetal is a Java-based framework for multi-objective optimization with metaheuristics

### The Multi-Objective Chernobyl Disaster Optimizer (MOCDO) is a multi-objective optimization algorithm derived from the Chernobyl Disaster Optimizer (CDO). It integrates the principles of multi-objective optimization to solve problems that involve balancing conflicting objectives.

1. Population Initialization
The algorithm begins by generating a population of candidate solutions randomly within the search space.

2. Evaluation of Objective Functions
Each candidate solution is evaluated based on multiple objective functions, producing a vector of objective values.

3. Iteration Process
The core of MOCDO consists of alternating strategies during the iteration process to balance exploration and exploitation:

3. Pareto Front Generation
After each iteration, a non-dominated sorting mechanism is used to classify solutions into different fronts based on Pareto dominance. Solutions that are not dominated by any other solutions are part of the Pareto front.

4. Crowding Distance Calculation
To maintain diversity in the Pareto front, the algorithm calculates the crowding distance for each solution. This ensures that solutions are spread across the front and do not cluster too closely together, leading to better exploration of trade-offs between objectives.

5. Stopping Criteria
The MOCDO repeats this process for a predefined number of iterations