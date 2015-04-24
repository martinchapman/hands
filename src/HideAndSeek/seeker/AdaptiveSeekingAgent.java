package HideAndSeek.seeker;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.AdaptiveGraphTraversingAgent;
import HideAndSeek.AdaptiveGraphTraverser;
import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.repeatgame.probability.adaptable.HighProbabilityAdaptable;
import Utility.Utils;

/**
 * @author Martin
 */
public class AdaptiveSeekingAgent<E extends Seeker & AdaptiveGraphTraverser> extends AdaptiveGraphTraversingAgent<E> implements Seeker {
	
	public AdaptiveSeekingAgent(
			GraphController<StringVertex, StringEdge> graphController,
			ArrayList<E> strategyPortfolio, int totalRounds, E currentStrategy) {
		super(graphController, strategyPortfolio, totalRounds, currentStrategy);
		// TODO Auto-generated constructor stub
	}

	public AdaptiveSeekingAgent(
			GraphController<StringVertex, StringEdge> graphController,
			ArrayList<E> strategyPortfolio, int totalRounds) {
		super(graphController, strategyPortfolio, totalRounds);
		// TODO Auto-generated constructor stub
	}

	public AdaptiveSeekingAgent(
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
	 * 
	 */
	@Override
	public void search() {
		
		currentStrategy.search();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#searchCriteria()
	 */
	public boolean searchCriteria() {
		
		return currentStrategy.searchCriteria();
		
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
		
		return "s" + getName();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#allHideLocations()
	 */
	@Override
	public ArrayList<StringVertex> allHideLocations() {
	
		return currentStrategy.allHideLocations();
	
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#uniqueHideLocations()
	 */
	@Override
	public HashSet<StringVertex> uniqueHideLocations() {

		return currentStrategy.uniqueHideLocations();
	
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.Seeker#mergeOtherTraverser(HideAndSeek.seeker.Seeker)
	 */
	@Override
	public void mergeOtherTraverser(Seeker traverser) {
		
		currentStrategy.mergeOtherTraverser(traverser);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraversingAgent#changeToOtherStrategy(HideAndSeek.GraphTraverser)
	 */
	public void changeToOtherStrategy(E strategy) {
		
		super.changeToOtherStrategy(strategy);
		
		currentStrategy.mergeOtherTraverser(previousStrategy);
		
	}

}