package HideAndSeek.seeker.repeatgame.probability.adaptable;

import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.AdaptiveSeekerStrategy;
import HideAndSeek.seeker.repeatgame.probability.VariableNodesInverseHighProbability;
import Utility.ScoreMetric;

/**
 * 
 * @author Martin
 *
 */
public class InverseHighProbabilityAdaptable extends VariableNodesInverseHighProbability implements AdaptiveSeekerStrategy {

	/**
	 * @param graphController
	 */
	public InverseHighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController, int predictiveNodes) {
		
		super(graphController, predictiveNodes);
		
		uniqueHideLocations = new HashSet<StringVertex>();
		
	}

	/**
	 * 
	 */
	HashSet<StringVertex> uniqueHideLocations;
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
		uniqueHideLocations.add(location);
		
		
	}
	
	/**
	 * The smaller the number of hide locations that remain, 
	 * the more likely that it is that a Hider is attempting
	 * to visit unique nodes. Therefore, we model the suitability
	 * of the strategy on this value.
	 * 
	 * @return
	 */
	@Override
	public double relevanceOfStrategy() {
	
		return (uniqueHideLocations.size() / (double)(graphController.vertexSet().size())); 
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfOpponent()
	 */
	@Override
	public double performanceOfOpponent() {

		return 0.0;
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.InverseHighProbability#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		uniqueHideLocations.clear();
		
		super.endOfGame();
		
	}

	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {

		return graphController.latestTraverserRoundPerformance(this, ScoreMetric.COST_CHANGE);
		
	}
	
}
