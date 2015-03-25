package HideAndSeek.hider;

import java.util.ArrayList;

import HideAndSeek.AdaptiveGraphTraverser;
import HideAndSeek.AdaptiveGraphTraverserStrategy;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

/**
 * @author Martin
 *
 */
public class AdaptiveHider<E extends Hider & AdaptiveGraphTraverserStrategy> extends AdaptiveGraphTraverser<E> implements Hider {

	public AdaptiveHider(
			GraphController<StringVertex, StringEdge> graphController,
			ArrayList<E> strategyPortfolio) {
		super(graphController, strategyPortfolio);
		// TODO Auto-generated constructor stub
	}

	public AdaptiveHider(
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
	 * @return
	 */
	@Override
	public ArrayList<StringVertex> getHideLocations() {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolioMinusCurrent ) {
				
				currentStrategy.getHideLocations();
				
			}
		
		}

		return currentStrategy.getHideLocations();
	
	}

	/**
	 * @return
	 */
	@Override
	public ArrayList<StringVertex> getExploredNodes() {
	
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolioMinusCurrent ) {
				
				currentStrategy.getExploredNodes();
				
			}
		
		}

		return currentStrategy.getExploredNodes();
	
	}

	/**
	 * @param location
	 */
	@Override
	public void addHideLocation(StringVertex location) {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolioMinusCurrent ) {
				
				currentStrategy.addHideLocation(location);
				
			}
		
		} else {
			
			currentStrategy.addHideLocation(location);
			
		}
		
	}

	/**
	 * @param vertex
	 * @return
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {
		
		if ( playAllStrategies ) {
			
			for ( E currentStrategy : strategyPortfolioMinusCurrent ) {
				
				currentStrategy.hideHere(currentNode);
				
			}
		
		}

		return currentStrategy.hideHere(vertex);
	
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
		
		return "h" + name;
		
	}

	
}