package HideAndSeek;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Metric;
import Utility.Pair;
import Utility.Utils;
import Utility.adaptive.AdaptiveMeasure;
import Utility.adaptive.AdaptiveWeightings;

/**
 * An adaptive graph traverser does not rely on its own mechanism to make
 * game decisions, but instead on a 'current strategy', selected from
 * a portfolio of existing other strategies.
 * 
 * This current strategy is changed for a different one in the portfolio
 * when it is no longer believed to be beneficial to the game. This is subject
 * to indication from the existing strategy, which has been modified to 
 * have the ability communicate whether it thinks adaptation is necessary.
 * 
 * Also serves to facilitate the random selection of an initial strategy.
 * To do this, a strategy should simply report -1 when asked for its view
 * on the necessity of adaptation. The only 'adaptation' will then be the
 * initial (random) choice of strategy.
 * 
 * @author Martin
 *
 */
public abstract class AdaptiveGraphTraversingAgent<E extends GraphTraverser & AdaptiveGraphTraverser> extends GraphTraversingAgent implements GraphTraverser {

	/**
	 * 
	 */
	protected E currentStrategy;
	
	/**
	 * 
	 */
	protected ArrayList<E> strategyPortfolio;
	
	/**
	 * 
	 */
	protected ArrayList<E> remainingStrategies;
	
	/**
	 * 
	 */
	protected E initialStrategy;
	
	/**
	 * 
	 */
	protected E previousStrategy;
	
	/**
	 * @deprecated
	 */
	protected double strategyRelevanceThreshold = 0.5;
	
	/**
	 * @deprecated
	 */
	protected double ownPerformanceThreshold = 0.5;
	
	/**
	 * @deprecated
	 */
	protected double opponentPerformanceThreshold = 0.5;
		 
	/**
	 * 
	 */
	protected double cueTriggerThreshold = 0.5;
	
	/**
	 * Whether a strategy can be returned to once it has been selected.
	 */
	protected boolean canReuse = false;
	
	/**
	 * 
	 */
	protected int totalRounds = 0;
	
	/**
	 * 
	 */
	LinkedHashMap<Integer, Pair<E, Double>> roundStrategyPerformance;
	
	/**
	 * @deprecated
	 */
	protected TreeMap<E, Double> strategyPayoff;
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param totalRounds
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, ArrayList<E> strategyPortfolio, int totalRounds, String initialStrategy) {
		
		super(graphController, name);
		
		remainingStrategies = new ArrayList<E>(strategyPortfolio);
		
		for ( E strategy : strategyPortfolio ) {
			
			if ( strategy.getName().equals(initialStrategy) ) {
				
				this.currentStrategy = strategy;
				
			}
			
		}
		
		if ( this.currentStrategy == null ) throw new UnsupportedOperationException("No match in portfolio for specified strategy " + initialStrategy);
		
		remainingStrategies.remove(currentStrategy);
		
		this.strategyPortfolio = strategyPortfolio;
		
		this.totalRounds = totalRounds;
		
		/* 
		 * Flag that the various costs accrued by any strategy
		 * employed should be borne by this adaptive strategy.
		 * They should also be deregistered as stand-alone strategies.
		 * 
		 */
		for ( E strategy : strategyPortfolio ) {
			
			strategy.setResponsibleAgent(this.responsibleAgent);
			
			graphController.deregisterTraversingAgent(strategy);
			
			Utils.talk(toString(), "Strategy: " + strategy);
			
		}
		
		strategyPayoff = new TreeMap<E, Double>();
		
		roundStrategyPerformance = new LinkedHashMap<Integer, Pair<E, Double>>();
		
		Utils.talk(toString(), "---> Using strategy: " + currentStrategy);
		
		this.strategyChanges = 0;
		
	}

	/**
	 * @param graphController
	 * @param name
	 * @param strategyPortfolio
	 * @param totalRounds
	 * @param currentStrategy
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio, int totalRounds, String currentStrategy) {
		
		this(graphController, "", strategyPortfolio, totalRounds, currentStrategy);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param strategyList
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, ArrayList<E> strategyPortfolio, int totalRounds) {
		
		this( graphController, name, strategyPortfolio, totalRounds, strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size())).getName());
		
	}
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param totalRounds
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio, int totalRounds) {
		
		this( graphController, "", strategyPortfolio, totalRounds, strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size())).getName());
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 * @param strategyPortfolio
	 * @param totalRounds
	 * @param cueTriggerThreshold
	 * @param canReuse
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, ArrayList<E> strategyPortfolio, int totalRounds, 
			double cueTriggerThreshold, boolean canReuse) {
		
		this( graphController, name, strategyPortfolio,totalRounds, strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size())).getName(), cueTriggerThreshold, canReuse );
		
	}
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param strategyRelevanceThreshold
	 * @param opponentPerformanceThreshold
	 * @param ownPerformanceThreshold
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, ArrayList<E> strategyPortfolio, int totalRounds, String initialStrategy, 
			double cueTriggerThreshold, boolean canReuse) {
		
		this(graphController, name, strategyPortfolio, totalRounds, initialStrategy);
		
		this.cueTriggerThreshold = cueTriggerThreshold;
		
		this.canReuse = canReuse;
			
	}
	
	/**
	 * @param graphController
	 * @param totalRounds
	 * @param initialStrategy
	 * @param strategyPortfolio
	 * @param strategyRelevanceThreshold
	 * @param opponentPerformanceThreshold
	 * @param ownPerformanceThreshold
	 * @param canReuse
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio, int totalRounds, String initialStrategy,
			double cueThreshold, boolean canReuse) {
		
		this( graphController, "", strategyPortfolio, totalRounds, initialStrategy, cueThreshold, canReuse);
			
	}
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param totalRounds
	 * @param cueThreshold
	 * @param canReuse
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio, int totalRounds, double cueThreshold, boolean canReuse) {
		
		this( graphController, "", strategyPortfolio, totalRounds, strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size())).getName(), cueThreshold, canReuse);
			
	}
	
	/**
	 * @return
	 */
	private Hashtable<E, Double> strategyToAveragePerformance() {
		
		Hashtable<E, Integer> numberOfTimesStrategyPlayed = new Hashtable<E, Integer>();
		
		Hashtable<E, Double> cumulativeStrategyPayoff = new Hashtable<E, Double>();
		
		for ( Entry<Integer, Pair<E, Double>> entry : roundStrategyPerformance.entrySet()) {
			
			E thisStrategy = entry.getValue().getElement0();
			
			Double thisStrategyRoundPayoff = entry.getValue().getElement1();
			
			if ( numberOfTimesStrategyPlayed.containsKey(thisStrategy) ) {
				
				numberOfTimesStrategyPlayed.put(thisStrategy, numberOfTimesStrategyPlayed.get(thisStrategy) + 1);
				
			} else {
				
				numberOfTimesStrategyPlayed.put(thisStrategy, 1);
				
			}
			
			if ( cumulativeStrategyPayoff.containsKey(entry.getValue().getElement0()) ) {
				
				cumulativeStrategyPayoff.put(thisStrategy, cumulativeStrategyPayoff.get(thisStrategy) + thisStrategyRoundPayoff);
				
			} else {
				
				cumulativeStrategyPayoff.put(thisStrategy, thisStrategyRoundPayoff);
				
			}
			
		}
		
		Hashtable<E, Double> averageStrategyPayoff = new Hashtable<E, Double>();
		
		for ( Entry<E, Double> cumulativePayoff : cumulativeStrategyPayoff.entrySet() ) {
			
			averageStrategyPayoff.put(cumulativePayoff.getKey(), cumulativePayoff.getValue() / numberOfTimesStrategyPlayed.get(cumulativePayoff.getKey()));
			
		}
		
		return averageStrategyPayoff;
		
	}
	
	/**
	 * @param strategy
	 * @return
	 */
	public ArrayList<Double> strategyToAllPerformances(E strategy) {
		
		ArrayList<Double> strategyToAllPerformances = new ArrayList<Double>();
		
		for ( Entry<Integer, Pair<E, Double>> entry : roundStrategyPerformance.entrySet()) {
			
			if ( entry.getValue().getElement0().equals(strategy)) {
			
				strategyToAllPerformances.add(entry.getValue().getElement1());
			
			}
			
		}
		
		return strategyToAllPerformances;
		
	}
	
	/**
	 * @return
	 */
	protected double confidenceLevel() {
		
		return currentStrategy.roundsPassed() / ( (double)(totalRounds) / 2 );
		
	}
	
	/**
	 * 
	 */
	protected void checkCurrentPerformance() {
		
		Utils.talk(toString(), "================================= Adaptive Assessment -- Current Strategy: " + currentStrategy + " =================================");
		
		roundStrategyPerformance.put(roundsPassed, new Pair<E, Double>(currentStrategy, graphController.latestTraverserRoundPerformance(responsibleAgent, Metric.PAYOFF)));
		
		double relevanceOfStrategy = currentStrategy.environmentalMeasure().getAdaptiveAssessment();
		
		double performanceOfOpponent = currentStrategy.socialMeasure().getAdaptiveAssessment();
		
		Utils.talk(toString(), "strategyToAveragePerformance() " + strategyToAveragePerformance());
		
		Utils.talk(toString(), "Utils.sortByValue(strategyToAveragePerformance(), true) " + Utils.sortByValue(strategyToAveragePerformance(), true));
		
		Hashtable<E, Double> strategyPayoff = strategyToAveragePerformance();
		 
	    Utils.talk(toString(), "All strategy payoffs: " + strategyPayoff);
	    
		double performanceOfSelf = 0.0;
		
	    E highestPerformingStrategy = new ArrayList<E>(strategyPayoff.keySet()).get(new ArrayList<E>(strategyPayoff.keySet()).size() - 1);
	    
	    Utils.talk(toString(), "Strategy with highest historic payoff: " + highestPerformingStrategy);
	    
		// Other strategy with highest average payoff is this one
		if ( highestPerformingStrategy.getName().equals(currentStrategy.getName()) ) {
			
			Utils.talk(toString(), "Defaulting to relative performance check, as highest performing strategy is this one.");
			
			// Default to relative performance check
			performanceOfSelf = currentStrategy.internalMeasure(strategyToAllPerformances(currentStrategy)).getAdaptiveAssessment();
			
		} else {
			
			double lastValue = new ArrayList<Double>(strategyPayoff.values()).get(new ArrayList<Double>(strategyPayoff.values()).size() - 1);
			
			// Compare average payoff to average payoff of highest strategy
			performanceOfSelf = Utils.percentageChange(strategyPayoff.get(currentStrategy), lastValue) / 100.0;
			
			if ( performanceOfSelf < 0 ) performanceOfSelf = 0.0;
			if ( performanceOfSelf > 1 ) performanceOfSelf = 1.0;
			
			Utils.talk(toString(), "Payoff Highest Strategy: Percentage change between " + strategyPayoff.get(currentStrategy) + " and " + lastValue + " = " + Utils.percentageChange(strategyPayoff.get(currentStrategy), lastValue));
		
		}
		
		AdaptiveWeightings currentStrategyWeightings = currentStrategy.getAdaptiveWeightings();
		
		Utils.talk(currentStrategy.toString(), "Relevance of strategy: " + relevanceOfStrategy + " Weighting: " + currentStrategyWeightings.getEnvironmentalWeighting());
		
		Utils.talk(currentStrategy.toString(), "Performance of opponent: " + performanceOfOpponent + " Weighting: " + currentStrategyWeightings.getSocialWeighting());
		
		Utils.talk(currentStrategy.toString(), "Performance of self: " + performanceOfSelf + " Weighting: " + currentStrategyWeightings.getInternalWeighting());
		
		double averageAssessmentValue = ( currentStrategyWeightings.getEnvironmentalWeighting() * relevanceOfStrategy ) + ( currentStrategyWeightings.getSocialWeighting() * performanceOfOpponent ) + ( currentStrategyWeightings.getInternalWeighting() * performanceOfSelf );
		
		Utils.talk(toString(), "Average assessment value: " + averageAssessmentValue);
		
		double confidenceLevel = confidenceLevel();
		
		if ( confidenceLevel >= 1.0 ) confidenceLevel = 1.0;
		
		Utils.talk(toString(), "Confidence level: " + confidenceLevel);
		
		double adaptationDecision = confidenceLevel * averageAssessmentValue;
		
		Utils.talk(toString(), "Adaptation Decision: " + adaptationDecision);
		
		if ( adaptationDecision > cueTriggerThreshold ) {
			
			Utils.talk(toString(), "Adapting!");
			
			Hashtable<Double, AdaptiveMeasure> weightingToMeasure = new Hashtable<Double, AdaptiveMeasure>();
			
			weightingToMeasure.put(currentStrategyWeightings.getEnvironmentalWeighting(), currentStrategy.environmentalMeasure());
			weightingToMeasure.put(currentStrategyWeightings.getSocialWeighting(), currentStrategy.socialMeasure());
			weightingToMeasure.put(currentStrategyWeightings.getInternalWeighting(), currentStrategy.internalMeasure(strategyToAllPerformances(currentStrategy)));
			
			double weightingRandom = Math.random();
			Utils.talk(toString(), "weightingRandom " + weightingRandom);
			
			ArrayList<Double> cumulativeWeights = new ArrayList<Double>();
			cumulativeWeights.add(0.0);
			
			for ( Entry<Double, AdaptiveMeasure> weightingToMeasureEntry : weightingToMeasure.entrySet() ) {
				
				Utils.talk(toString(), "weightingToMeasureEntry " + weightingToMeasureEntry);
				
				cumulativeWeights.add(cumulativeWeights.get(cumulativeWeights.size() - 1) + weightingToMeasureEntry.getKey());
				
				Utils.talk(toString(), "cumulativeWeights.get(cumulativeWeights.size() - 2) " + cumulativeWeights.get(cumulativeWeights.size() - 2));
				Utils.talk(toString(), "cumulativeWeights.get(cumulativeWeights.size() - 1) " + cumulativeWeights.get(cumulativeWeights.size() - 1));
				
				if ( weightingRandom > cumulativeWeights.get(cumulativeWeights.size() - 2) && weightingRandom < cumulativeWeights.get(cumulativeWeights.size() - 1) ) {
					
					Utils.talk(toString(), weightingToMeasureEntry + " selected.");
					
					if ( weightingToMeasureEntry.getValue().adaptToSpecified() ) { 
						
						for ( E strategy : strategyPortfolio ) { 
							
							if ( strategy.getName().equals(weightingToMeasureEntry.getValue().getAdaptTo()) ) { 
								
								changeToOtherStrategy(strategy);
								
								return;
							
							}
							
						}
						
						throw new UnsupportedOperationException("Adapt to strategy not found in portfolio.");
						
					} else {
						
						Utils.talk(toString(), weightingToMeasureEntry + " no adapt to strategy specified.");
						
					}
					
				}
				
			}
			
			if ( !changeToOtherStrategy() ) {
				
				if ( highestPerformingStrategy.getName().equals(currentStrategy.getName()) ) {
					
					Utils.talk(toString(), "No random strategy selections left. Highest Performing is this strategy. Not changing.");
					
				} else {
					
					Utils.talk(toString(), "No random strategy selections left. Changing to highest performing strategy: " + highestPerformingStrategy);
					
					changeToOtherStrategy(highestPerformingStrategy);
					
				}
				
				return;
				
			}
			
			Utils.talk(toString(), "Selecting other strategy from portfolio at random");
			
		} else {
			
			Utils.talk(toString(), "Not adapting");
			
		}
		
		Utils.talk(toString(), "==================================================================");
		
	}
	
	/**
	 * Asks the current strategy for its opinion on whether adaptation is necessary
	 * based upon the three factors described in the adaptation interface.
	 * If any of these measures exceeds the given threshold, the strategy is altered
	 * according to a predefined mechanism (e.g. randomly, excluding existing).
	 * 
	 * Incorporates the fact that strategies that have not been playing for a 
	 * significant period may not have the most accurate opinion using a probability
	 * weighting based upon the rounds passed. That is, with more rounds passed, the 
	 * view of the current strategy is more likely to be adhered to. For example,
	 * a small number of hide locations is more likely to indicate repeat play after
	 * 50 rounds, than after 1.
	 * 
	 * @deprecated
	 */
	protected void checkCurrentPerformance_Org() {
		
		double relevanceOfStrategy = currentStrategy.environmentalMeasure().getAdaptiveAssessment();
		
		double performanceOfOpponent = currentStrategy.socialMeasure().getAdaptiveAssessment();
		
		double performanceOfSelf = currentStrategy.internalMeasure(null).getAdaptiveAssessment();
		
		Utils.talk(currentStrategy.toString(), "Relevance of strategy: " + relevanceOfStrategy);
		
		Utils.talk(currentStrategy.toString(), "Performance of opponent: " + performanceOfOpponent);
		
		Utils.talk(currentStrategy.toString(), "Performance of self: " + performanceOfSelf);
		
		/* 
		 * Record payoff, later used to compare performance in light of signalled adaptation by all strategies 
		 * (i.e. no unused strategies remain).
		 */
		strategyPayoff.put(currentStrategy, ( currentStrategy.environmentalMeasure().getAdaptiveAssessment() * ( 1.0 - currentStrategy.socialMeasure().getAdaptiveAssessment() ) * currentStrategy.internalMeasure(null).getAdaptiveAssessment() ) / 3 );
		
		if ( ( relevanceOfStrategy < strategyRelevanceThreshold && currentStrategy.environmentalMeasure().getAdaptiveAssessment() > -1 ) || 
				
			 ( performanceOfOpponent > opponentPerformanceThreshold && currentStrategy.socialMeasure().getAdaptiveAssessment() > -1 ) ||
			 
			 ( performanceOfSelf < ownPerformanceThreshold && currentStrategy.internalMeasure(null).getAdaptiveAssessment() > -1 ) ) { 
			 
			 Utils.talk(currentStrategy.toString(), "Requires change. Confidence level: " + (currentStrategy.roundsPassed() / (double)(totalRounds)));
			 
			 if (Math.random() < (currentStrategy.roundsPassed() / (double)(totalRounds)) ) {
				 
				 if ( remainingStrategies.size() > 0 ) {
					 
					 changeToOtherStrategy();
				 
				 } else {
					
					 strategyPayoff = new TreeMap<E, Double>(Utils.sortByValue(strategyPayoff, true));
					 
					 changeToOtherStrategy(strategyPayoff.firstKey());
					 
				 }
				 
			 } else {
				 
				 Utils.talk(toString(), "Confidence not high enough to change from " + currentStrategy);
				 
			 }
			 
		}
	
	}
	
	/**
	 * 
	 */
	private int strategyChanges;
	
	/**
	 * Change to a random strategy
	 */
	public boolean changeToOtherStrategy() {
		
		if ( remainingStrategies.size() == 0 ) return false;
		
		changeToOtherStrategy(remainingStrategies.get((int)(Math.random()*remainingStrategies.size())));
		
		if ( canReuse ) remainingStrategies = new ArrayList<E>(strategyPortfolio);
			
		remainingStrategies.remove(currentStrategy);
		
		return true;
		
	}
	
	/**
	 * @param strategy
	 */
	public void changeToOtherStrategy(E strategy) {
		
		strategyChanges++;
		
		currentStrategy.stopStrategy();
		
		// Attempt to transfer the knowledge we gained from the last strategy to the new one.
		currentStrategy.mergeOtherTraverser(previousStrategy);
		
		currentStrategy = strategy;
		
		Utils.talk(toString(), "Changing from " + previousStrategy + " to: " + currentStrategy);
				
	}
	
	/**
	 * 
	 */
	public void endOfRound() {
		
		currentStrategy.endOfRound();
			
		checkCurrentPerformance();
		
		super.endOfRound();
		
	}

	/**
	 * 
	 */
	public void endOfGame() {
			
		currentStrategy.endOfGame();
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		if ( currentStrategy != previousStrategy ) {
			
			previousStrategy = currentStrategy;
			
			return currentStrategy.printRoundStats() + ",StrategyChange,1";
		
		} else {
		
			previousStrategy = currentStrategy;
			
			return currentStrategy.printRoundStats() + ",StrategyChange,0";
		
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#printGameStats()
	 */
	@Override
	public String printGameStats() {

		return currentStrategy.printGameStats() + ",FinalStrategy-" + currentStrategy + ",1";

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		currentStrategy.run();
		
	}
	
	/**
	 * @return
	 */
	@Override
	protected ArrayList<StringVertex> hideLocations() {
	
		return currentStrategy.requestHideLocations(responsibleAgent);
	
	}
	
	/**
	 * @return
	 */
	@Override
	public ArrayList<StringVertex> exploredNodes() {
	
		return currentStrategy.exploredNodes();
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public abstract StringVertex nextNode(StringVertex currentNode);

}