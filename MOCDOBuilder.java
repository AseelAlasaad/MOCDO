package cdo;


import org.uma.jmetal.algorithm.AlgorithmBuilder;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.NonUniformMutation;
import org.uma.jmetal.operator.mutation.impl.UniformMutation;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

/** Class implementing the MOCDO algorithm */
public class MOCDOBuilder implements AlgorithmBuilder<MOCDO> {
  protected DoubleProblem problem;
  protected SolutionListEvaluator<DoubleSolution> evaluator;

  private int swarmSize = 100 ;
  private int archiveSize = 100 ;
  private int maxIterations = 25000 ;

  private double eta = 0.0075 ;

  private UniformMutation uniformMutation ;
  private NonUniformMutation nonUniformMutation ;

  public MOCDOBuilder(DoubleProblem problem, SolutionListEvaluator<DoubleSolution> evaluator) {
    this.evaluator = evaluator ;
    this.problem = problem ;
  }

  public MOCDOBuilder setSwarmSize(int swarmSize) {
    this.swarmSize = swarmSize ;

    return this ;
  }

  public MOCDOBuilder setArchiveSize(int archiveSize) {
    this.archiveSize = archiveSize ;

    return this ;
  }

  public MOCDOBuilder setMaxIterations(int maxIterations) {
    this.maxIterations = maxIterations ;

    return this ;
  }

  public MOCDOBuilder setUniformMutation(MutationOperator<DoubleSolution> uniformMutation) {
    this.uniformMutation = (UniformMutation)uniformMutation ;

    return this ;
  }

  public MOCDOBuilder setNonUniformMutation(MutationOperator<DoubleSolution> nonUniformMutation) {
    this.nonUniformMutation = (NonUniformMutation)nonUniformMutation ;

    return this ;
  }

  public MOCDOBuilder setEta(double eta) {
    this.eta = eta ;

    return this ;
  }

  /* Getters */
  public int getArchiveSize() {
    return archiveSize;
  }

  public int getSwarmSize() {
    return swarmSize;
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public UniformMutation getUniformMutation() {
    return uniformMutation;
  }

  public NonUniformMutation getNonUniformMutation() {
    return nonUniformMutation;
  }

  public MOCDO build() {
    return new MOCDO(problem, evaluator, swarmSize, maxIterations, archiveSize, eta, uniformMutation,
        nonUniformMutation) ;
  }
}