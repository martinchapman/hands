package HideAndSeek;

import java.util.ArrayList;
import java.util.List;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import Utility.ScoreMetric;
import Utility.Utils;

/**
 * An adaptive graph traverser does not rely on its own mechanism to make
 * game decisions, but instead on a 'current strategy', selected from
 * a portfolio of strategies.
 * 
 * This current strategy is changed for a different one in the portfolio
 * when it is no longer believed to be beneficial to the game.
 * 
 * @author Martin
 *
 */
public abstract class AdaptiveGraphTraverser<E extends GraphTraverser & AdaptiveGraphTraverserStrategy> extends GraphTraversingAgent {

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
	protected ArrayList<E> strategyPortfolioMinusCurrent;
	
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
	 * Whether all strategies should be played, with the results
	 * only being taken from one, or whether only a single strategy
	 * should be incremented.
	 */
	protected boolean playAllStrategies = true;
	
	/**
	 * 
	 */
	protected int totalRounds = 0;
	
	/**
	 * @param graphController
	 * @param numberOfHideLocations
	 * @param strategyList
	 */
	public AdaptiveGraphTraverser( GraphController<StringVertex, StringEdge> graphController, ArrayList<E> strategyPortfolio) {
		
		super(graphController);
		
		this.strategyPortfolio = strategyPortfolio;
		
		currentStrategy = strategyPortfolio.get((int)(Math.random()*strategyPortfolio.size()));
		
		strategyPortfolioMinusCurrent = new ArrayList<E>(strategyPortfolio);
		
		strategyPortfolioMinusCurrent.remove(currentStrategy);
		
		//Utils.talk(this.toString(), "Starting with strategy: " + currentStrategy);
	
	}
	
	/**
	 * @param graphController
	 * @param strategyPortfolio
	 * @param strategyRelevanceThreshold
	 * @param opponentPerformanceThreshold
	 * @param ownPerformanceThreshold
	 */
	public AdaptiveGraphTraverser( GraphController<StringVertex, StringEdge> graphController, int totalRounds, E initialStrategy, ArrayList<E> strategyPortfolio, 
			double strategyRelevanceThreshold, double opponentPerformanceThreshold, double ownPerformanceThreshold, boolean canReuse) {
		
		this(graphController, strategyPortfolio);
		
		this.strategyRelevanceThreshold = strategyRelevanceThreshold;
		
		this.opponentPerformanceThreshold = opponentPerformanceThreshold;
		
		this.ownPerformanceThreshold = ownPerformanceThreshold;
		
		this.initialStrategy = initialStrategy; 
		
		this.currentStrategy = initialStrategy;
		
		this.previousStrategy = initialStrategy;
		
		this.canReuse = canReuse;
		
		this.totalRounds = totalRounds;
		
		this.strategyChanges = 0;
				
		Utils.talk(this.toString(), "--> Starting with strategy: " + currentStrategy);
	
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
		
		Utils.talk(currentStrategy.toString(), "Relevance of strategy: " + currentStrategy.relevanceOfStrategy());
		
		Utils.talk(currentStrategy.toString(), "Performance of opponent: " + currentStrategy.performanceOfOpponent());
		
		Utils.talk(currentStrategy.toString(), "Performance of self: " + currentStrategy.performanceOfSelf());
		
		if ( currentStrategy.relevanceOfStrategy() < strategyRelevanceThreshold || 
				
			 currentStrategy.performanceOfOpponent() > opponentPerformanceThreshold ||
			 
			 currentStrategy.performanceOfSelf() < ownPerformanceThreshold ) { 
			 
			 Utils.talk(currentStrategy.toString(), "Requires change. Confidence level: " + (currentStrategy.getRoundsPassed() / (double)(totalRounds)));
			 
			 if (Math.random() < (currentStrategy.getRoundsPassed() / (double)(totalRounds)) ) {
				 
				 changeToRandomOtherStrategy();
				 
			 } else {
				 
				 Utils.talk(this.toString(), "Confidence not high enough to change from " + currentStrategy);
				 
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
	public void changeToRandomOtherStrategy() {
		
		strategyChanges++;
		
		currentStrategy = strategyPortfolioMinusCurrent.get((int)(Math.random()*strategyPortfolioMinusCurrent.size()));
		
		strategyPortfolioMinusCurrent = new ArrayList<E>(strategyPortfolio);
		
		strategyPortfolioMinusCurrent.remove(currentStrategy);
		
		Utils.talk(this.toString(), "Changing from " + previousStrategy + " to: " + currentStrategy);
		
	}
	
	/**
	 * 
	 */
	public void endOfRound() {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolio ) {
				
				currentStrategy.endOfRound();
				
			}
		
		} else {
			
			currentStrategy.endOfRound();
			
		}
		
		checkCurrentPerformance();
		
	}

	/**
	 * 
	 */
	public void endOfGame() {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolio ) {
				
				currentStrategy.endOfGame();
				
			}
		
		} else {
			
			currentStrategy.endOfGame();
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#printRoundStats()
	 */
	@Override
	public String printRoundStats() {
		
		if ( currentStrategy != previousStrategy ) {
			
			System.out.println(currentStrategy + " not equal to " + previousStrategy);
			
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
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolio ) {
				
				currentStrategy.run();
				
			}
		
		} else {
			
			currentStrategy.run();
			
		}
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#nextNode(HideAndSeek.graph.StringVertex)
	 */
	@Override
	public abstract StringVertex nextNode(StringVertex currentNode);

}