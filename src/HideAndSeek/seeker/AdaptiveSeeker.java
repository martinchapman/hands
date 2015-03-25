package HideAndSeek.seeker;

import java.util.ArrayList;

import HideAndSeek.AdaptiveGraphTraverser;
import HideAndSeek.AdaptiveGraphTraverserStrategy;
import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.repeatgame.probability.adaptable.HighProbabilityAdaptable;

/**
 * @author Martin
 */
public class AdaptiveSeeker<E extends Seeker & AdaptiveGraphTraverserStrategy> extends AdaptiveGraphTraverser<E> implements Seeker {
	
	public AdaptiveSeeker(
			GraphController<StringVertex, StringEdge> graphController,
			ArrayList<E> strategyPortfolio) {
		super(graphController, strategyPortfolio);
		// TODO Auto-generated constructor stub
	}

	public AdaptiveSeeker(
			GraphController<StringVertex, StringEdge> graphController,
			int totalRounds, E initialStrategy, ArrayList<E> strategyPortfolio,
			double strategyRelevanceThreshold,
			double opponentPerformanceThreshold,
			double ownPerformanceThreshold, boolean canReuse) {
		super(graphController, totalRounds, initialStrategy, strategyPortfolio,
				strategyRelevanceThreshold, opponentPerformanceThreshold,
				ownPerformanceThreshold, canReuse);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param location
	 */
	@Override
	public void addHideLocation(StringVertex location) {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolio ) {
				
				currentStrategy.addHideLocation(currentNode);
				
			}
		
		} else {
			
			currentStrategy.addHideLocation(location);
			
		}
		
	}

	/**
	 * 
	 */
	@Override
	public void search() {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolio ) {
				
				currentStrategy.search();
				
			}
		
		} else {
			
			currentStrategy.search();
		
		}
	
	}

	/**
	 * @param currentNode
	 * @return
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolioMinusCurrent ) {
				
				currentStrategy.nextNode(currentNode);
				
			}
		
		}
		
		return currentStrategy.nextNode(currentNode);
	
	}

	/**
	 * @return
	 */
	@Override
	public StringVertex startNode() {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolioMinusCurrent ) {
				
				currentStrategy.startNode();
				
			}
		
		}

		return currentStrategy.startNode();
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#toString()
	 */
	public String toString() {
		
		return "s" + name;
		
	}

}