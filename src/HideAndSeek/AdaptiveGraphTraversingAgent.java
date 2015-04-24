package HideAndSeek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.Utils;

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
 * on the necessity of adapatation. The only 'adaptation' will then be the
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
	 * 
	 */
	protected double strategyRelevanceThreshold = 0.5;
	
	/**
	 * 
	 */
	protected double opponentPerformanceThreshold = 0.5;
	
	/**
	 * 
	 */
	protected double ownPerformanceThreshold = 0.5;
	
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
	protected TreeMap<E, Double> strategyPayoff;
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param totalRounds
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, ArrayList<E> strategyPortfolio, int totalRounds, E currentStrategy) {
		
		super(graphController, name);
		
		remainingStrategies = new ArrayList<E>(strategyPortfolio);
		
		this.currentStrategy = currentStrategy;
		
		remainingStrategies.remove(currentStrategy);
		
		this.initialStrategy = initialStrategy; 
		
		this.previousStrategy = initialStrategy;
		
		this.strategyPortfolio = strategyPortfolio;
		
		this.totalRounds = totalRounds;
		
		/* 
		 * Flag that the various costs accrued by any strategy
		 * employed should be borne by this adaptive strategy.
		 * They should also be deregistered as stand-alone strategies.
		 * 
		 */
		for ( E strategy : strategyPortfolio ) {
			
			strategy.setResponsibleAgent(this);
			
			graphController.deregisterTraversingAgent(strategy);
			
			Utils.talk(toString(), "Strategy: " + strategy);
			
		}
		
		strategyPayoff = new TreeMap<E, Double>();
		
		Utils.talk(toString(), "Using strategy: " + currentStrategy);
		
		this.strategyChanges = 0;
		
	}
	
	/**
	 * @param graphController
	 * @param name
	 * @param strategyPortfolio
	 * @param totalRounds
	 * @param currentStrategy
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio, int totalRounds, E currentStrategy) {
		
		this(graphController, "", strategyPortfolio, totalRounds, currentStrategy);
		
	}
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param strategyList
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, ArrayList<E> strategyPortfolio, int totalRounds) {
		
		this( graphController, name, strategyPortfolio, totalRounds, strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size())));
		
	}
	
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio, int totalRounds) {
		
		this( graphController, "", strategyPortfolio, totalRounds, strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size())));
		
	}
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param strategyRelevanceThreshold
	 * @param opponentPerformanceThreshold
	 * @param ownPerformanceThreshold
	 */
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, String name, int totalRounds, E initialStrategy, ArrayList<E> strategyPortfolio, 
			double strategyRelevanceThreshold, double opponentPerformanceThreshold, double ownPerformanceThreshold, boolean canReuse) {
		
		this(graphController, name, strategyPortfolio, totalRounds, initialStrategy);
		
		this.strategyRelevanceThreshold = strategyRelevanceThreshold;
		
		this.opponentPerformanceThreshold = opponentPerformanceThreshold;
		
		this.ownPerformanceThreshold = ownPerformanceThreshold;
		
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
	public AdaptiveGraphTraversingAgent( GraphController<StringVertex, StringEdge> graphController, int totalRounds, E initialStrategy, ArrayList<E> strategyPortfolio, 
			double strategyRelevanceThreshold, double opponentPerformanceThreshold, double ownPerformanceThreshold, boolean canReuse) {
		
		this( graphController, "", totalRounds, initialStrategy, strategyPortfolio, strategyRelevanceThreshold, opponentPerformanceThreshold, ownPerformanceThreshold, canReuse);
			
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
	 */
	protected void checkCurrentPerformance() {
		
		double relevanceOfStrategy = currentStrategy.relevanceOfStrategy();
		
		double performanceOfOpponent = currentStrategy.performanceOfOpponent();
		
		double performanceOfSelf = currentStrategy.performanceOfSelf();
		
		Utils.talk(currentStrategy.toString(), "Relevance of strategy: " + relevanceOfStrategy);
		
		Utils.talk(currentStrategy.toString(), "Performance of opponent: " + performanceOfOpponent);
		
		Utils.talk(currentStrategy.toString(), "Performance of self: " + performanceOfSelf);
		
		/* 
		 * Record payoff, later used to compare performance in light of signaled adaptation by all strategies 
		 * (i.e. no unused strategies remain).
		 */
		strategyPayoff.put(currentStrategy, ( currentStrategy.relevanceOfStrategy() * ( 1.0 - currentStrategy.performanceOfOpponent() ) * currentStrategy.performanceOfSelf() ) / 3 );
		
		if ( ( relevanceOfStrategy < strategyRelevanceThreshold && currentStrategy.relevanceOfStrategy() > -1 ) || 
				
			 ( performanceOfOpponent > opponentPerformanceThreshold && currentStrategy.performanceOfOpponent() > -1 ) ||
			 
			 ( performanceOfSelf < ownPerformanceThreshold && currentStrategy.performanceOfSelf() > -1 ) ) { 
			 
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
	 * 
	 */
	public void changeToOtherStrategy() {
		
		if ( remainingStrategies.size() == 0 ) return;
		
		changeToOtherStrategy(remainingStrategies.get((int)(Math.random()*remainingStrategies.size())));
		
		if ( canReuse ) remainingStrategies = new ArrayList<E>(strategyPortfolio);
			
		remainingStrategies.remove(currentStrategy);
		
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

		return currentStrategy.printGameStats();

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