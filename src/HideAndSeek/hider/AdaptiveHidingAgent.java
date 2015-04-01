package HideAndSeek.hider;

import java.util.ArrayList;

import HideAndSeek.AdaptiveGraphTraversingAgent;
import HideAndSeek.AdaptiveGraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

/**
 * @author Martin
 *
 */
public class AdaptiveHidingAgent<E extends Hider & AdaptiveGraphTraverser> extends AdaptiveGraphTraversingAgent<E> implements Hider {

	public AdaptiveHidingAgent(
			GraphController<StringVertex, StringEdge> graphController,
			ArrayList<E> strategyPortfolio, int totalRounds, E currentStrategy) {
		super(graphController, strategyPortfolio, totalRounds, currentStrategy);
		// TODO Auto-generated constructor stub
	}

	public AdaptiveHidingAgent(
			GraphController<StringVertex, StringEdge> graphController,
			ArrayList<E> strategyPortfolio, int totalRounds) {
		super(graphController, strategyPortfolio, totalRounds);
		// TODO Auto-generated constructor stub
	}

	public AdaptiveHidingAgent(
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
		
		currentStrategy.addHideLocation(location);
	
	}

	/**
	 * @param vertex
	 * @return
	 */
	@Override
	public boolean hideHere(StringVertex vertex) {
		
		return currentStrategy.hideHere(vertex);
	
	}

	/**
	 * @param currentNode
	 * @return
	 */
	@Override
	public StringVertex nextNode(StringVertex currentNode) {
		
		return currentStrategy.nextNode(currentNode);
	
	}

	/**
	 * @return
	 */
	@Override
	public StringVertex startNode() {
		
		return currentStrategy.startNode();
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.GraphTraversingAgent#toString()
	 */
	public String toString() {
		
		return "h" + name;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#numberOfHideLocations()
	 */
	@Override
	public int numberOfHideLocations() {

		return currentStrategy.numberOfHideLocations();
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.hider.Hider#mergeOtherTraverser(HideAndSeek.hider.Hider)
	 */
	@Override
	public void mergeOtherTraverser(Hider traverser) {
		
		traverser.mergeOtherTraverser(traverser);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraversingAgent#changeToOtherStrategy(HideAndSeek.GraphTraverser)
	 */
	public void changeToOtherStrategy(E strategy) {
		
		super.changeToOtherStrategy(strategy);
		
		currentStrategy.mergeOtherTraverser(previousStrategy);
		
	}
	
}