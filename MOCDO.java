package cdo;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.uma.jmetal.operator.mutation.impl.NonUniformMutation;
import org.uma.jmetal.operator.mutation.impl.UniformMutation;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.bounds.Bounds;
import org.uma.jmetal.util.comparator.dominanceComparator.impl.DominanceWithConstraintsComparator;
import org.uma.jmetal.util.comparator.dominanceComparator.impl.EpsilonDominanceComparator;
import org.uma.jmetal.util.densityestimator.DensityEstimator;
import org.uma.jmetal.util.densityestimator.impl.CrowdingDistanceDensityEstimator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
/** Class implementing the MOCDO algorithm */
@SuppressWarnings("serial")
public class MOCDO extends AbstractCDO<DoubleSolution, List<DoubleSolution>> {

  private DoubleProblem problem;

  SolutionListEvaluator<DoubleSolution> evaluator;

  private final int swarmSize;
  private int maxIterations;
  private int currentIteration;

  private DoubleSolution[] localBest;
  private CrowdingDistanceArchive<DoubleSolution> leaderArchive;
  private NonDominatedSolutionListArchive<DoubleSolution> epsilonArchive;

  private double[][] speed;

  private final Comparator<DoubleSolution> dominanceComparator;
  private final Comparator<DoubleSolution> crowdingDistanceComparator;

  private final UniformMutation uniformMutation ;
  private final NonUniformMutation nonUniformMutation;

  private double eta ;

  private JMetalRandom randomGenerator;
  private DensityEstimator<DoubleSolution> crowdingDistance;
  private double r1Max;
  private double r1Min;
  private double r2Max;
  private double r2Min;
  private double a;
  private Random random = new Random();

  private DoubleSolution[] globalBest ;
  private DoubleSolution[] gamma ;

  /** Constructor */
  public MOCDO(DoubleProblem problem, SolutionListEvaluator<DoubleSolution> evaluator,
      int swarmSize, int maxIterations, int archiveSize, double eta, UniformMutation uniformMutation,
      NonUniformMutation nonUniformMutation) {
    this.problem = problem ;
    this.evaluator = evaluator ;

    this.swarmSize = swarmSize ;
    this.maxIterations = maxIterations ;

    this.eta = eta ;

    this.uniformMutation = uniformMutation ;
    this.nonUniformMutation = nonUniformMutation ;

    localBest = new DoubleSolution[swarmSize];
    globalBest = new DoubleSolution[swarmSize] ;
    gamma = new DoubleSolution[swarmSize] ;
    leaderArchive = new CrowdingDistanceArchive<>(archiveSize);
    epsilonArchive = new NonDominatedSolutionListArchive<>(new EpsilonDominanceComparator<>(eta));

    crowdingDistance = new CrowdingDistanceDensityEstimator<>();

    dominanceComparator = new DominanceWithConstraintsComparator<>();
    crowdingDistanceComparator = crowdingDistance.comparator();
    

    speed = new double[swarmSize][problem.numberOfVariables()];
    
    this.r1Max = 1.0;
    this.r1Min = 0;
    this.r2Max = 1.0;
    this.r2Min = 0;
    this.a=0;

    randomGenerator = JMetalRandom.getInstance() ;
  
  }


  @Override protected void initProgress() {
    currentIteration = 1;
    crowdingDistance.compute(leaderArchive.solutions());
  }

  @Override protected void updateProgress() {
    currentIteration += 1;
    crowdingDistance.compute(leaderArchive.solutions());
  }

  @Override protected boolean isStoppingConditionReached() {
    return currentIteration >= maxIterations;
  }

  @Override
  protected List<DoubleSolution> createInitialSwarm() {
    List<DoubleSolution> swarm = new ArrayList<>(swarmSize);

    DoubleSolution newSolution;
    for (int i = 0; i < swarmSize; i++) {
      newSolution = problem.createSolution();
      swarm.add(newSolution);
    }

    return swarm;
  }

  @Override
  protected List<DoubleSolution> evaluateSwarm(List<DoubleSolution> swarm) {
    swarm = evaluator.evaluate(swarm, problem);
    return swarm ;
  }

  @Override public List<DoubleSolution> result() {
    //return this.leaderArchive.getSolutionList();
      return this.epsilonArchive.solutions();
  }

  @Override
  protected void initializeLeader(List<DoubleSolution> swarm) {
    for (DoubleSolution solution : swarm) {
      DoubleSolution particle = (DoubleSolution) solution.copy();
      if (leaderArchive.add(particle)) {
        epsilonArchive.add((DoubleSolution) particle.copy());
      }
    }
  }

  @Override
  protected void initializeParticlesMemory(List<DoubleSolution> swarm)  {
    for (int i = 0; i < swarm.size(); i++) {
      DoubleSolution particle = (DoubleSolution) swarm.get(i).copy();
      localBest[i] = particle;
    }
  }

  @Override
  protected void CDOalgorithm(List<DoubleSolution> swarm)  {
	    a = 3.0 - 3.0 * this.currentIteration / this.maxIterations;
	    
	    for (int i = 0; i < swarmSize; i++) {
	    	
	    	  DoubleSolution particle = getSwarm().get(i) ;
	    	  DoubleSolution bestParticle = (DoubleSolution) localBest[i];
		      DoubleSolution bestGlobal;
		      DoubleSolution one ;
		      DoubleSolution two;
		      int pos1 = randomGenerator.nextInt(0, leaderArchive.solutions().size() - 1);
		      int pos2 = randomGenerator.nextInt(0, leaderArchive.solutions().size() - 1);
		      one = leaderArchive.solutions().get(pos1);
		      two = leaderArchive.solutions().get(pos2);
	
		      if (crowdingDistanceComparator.compare(one, two) < 1) {
		        bestGlobal = one ;
		      } else {
		        bestGlobal = two ;
		      }
	  
		  
		   
		    
		    
			double a1 = Math.log10((16000 - 1) * random.nextDouble() + 16000);
		    double a2 = Math.log10((270000 - 1) * random.nextDouble() + 270000);
		    double a3 = Math.log10((300000 - 1) * random.nextDouble() + 300000);
		    for (int var = 0; var < particle.variables().size(); var++) {
		    	Bounds<Double> bounds = problem.variableBounds().get(var) ;
		    	double ub = bounds.getUpperBound() ;
		        double lb = bounds.getLowerBound();
	
		        double Alpha_pos = bestGlobal.variables().get(var);
		        double Beta_pos = bestParticle.variables().get(var);
		        double Gamma_pos = bestGlobal.variables().get(var);
	
		        double r1 = randomGenerator.nextDouble(r1Min, r1Max);
		        double r2 = randomGenerator.nextDouble(r2Min, r2Max);
		        double pa = Math.PI * r1 * r1 / (0.25 * a1) - this.a * random.nextDouble();
		        double C1 = r2 * r2 * Math.PI;
	
		        double D_alpha = Math.abs(C1 * Alpha_pos - particle.variables().get(var));
		        double va = 0.25 * (Alpha_pos - pa * D_alpha);
	
		        double r3 = randomGenerator.nextDouble(r1Min, r1Max);
		        double r4 = randomGenerator.nextDouble(r2Min, r2Max);
		        double pb = Math.PI * r3 * r3 / (0.5 * a2) - this.a * random.nextDouble();
		        double C2 = r4 * r4 * Math.PI;
	
		        double D_beta = Math.abs(C2 * Beta_pos - particle.variables().get(var));
		        double vb = 0.5 * (Beta_pos - pb * D_beta);
	
		        double r5 = randomGenerator.nextDouble(r1Min, r1Max);
		        double r6 = randomGenerator.nextDouble(r2Min, r2Max);
		        double py = Math.PI * r5 * r5 / a3 - this.a * random.nextDouble();
		        double C3 = r6 * r6 * Math.PI;
	
		        double D_gamma = Math.abs(C3 * Gamma_pos - particle.variables().get(var));
		        double vy = Gamma_pos - py * D_gamma;
		        
	
		  
		        particle.variables().set(var, (va + vb + vy) / 3.0);
	  }
	    }
  }
  /** Update the position of each particle */
  @Override
  protected void updatePosition(List<DoubleSolution> swarm)  {
    for (int i = 0; i < swarmSize; i++) {
      DoubleSolution particle = swarm.get(i);
      for (int var = 0; var < particle.variables().size(); var++) {
    
        Bounds<Double> bounds = problem.variableBounds().get(var) ;
        Double lowerBound = bounds.getLowerBound() ;
        Double upperBound = bounds.getUpperBound() ;
        if (particle.variables().get(var) < lowerBound) {
          particle.variables().set(var, lowerBound);
        
        }
        if (particle.variables().get(var) > upperBound) {
          particle.variables().set(var, upperBound);
        
        }
      }
    }
  }

  @Override
  protected void updateParticlesMemory(List<DoubleSolution> swarm) {
    for (int i = 0; i < swarm.size(); i++) {
      int flag = dominanceComparator.compare(swarm.get(i), localBest[i]);
      if (flag != 1) {
        DoubleSolution particle = (DoubleSolution) swarm.get(i).copy();
        localBest[i] = particle;
      }
    }
  }



  /**  Apply a mutation operator to all particles in the swarm (perturbation) */
  @Override
  protected void perturbation(List<DoubleSolution> swarm)  {
    nonUniformMutation.setCurrentIteration(currentIteration);

    for (int i = 0; i < swarm.size(); i++) {
      if (i % 3 == 0) {
        nonUniformMutation.execute(swarm.get(i));
      } else if (i % 3 == 1) {
        uniformMutation.execute(swarm.get(i));
      }
    }
  }

  /**
   * Update leaders method
   * @param swarm List of solutions (swarm)
   */
  @Override protected void updateLeaders(List<DoubleSolution> swarm) {
    for (DoubleSolution solution : swarm) {
      DoubleSolution particle = (DoubleSolution) solution.copy();
      if (leaderArchive.add(particle)) {
        epsilonArchive.add((DoubleSolution) particle.copy());
      }
    }
  }

  @Override public String name() {
    return "MOCDO" ;
  }

  @Override public String description() {
    return "Optimized MOCDO" ;
  }

}