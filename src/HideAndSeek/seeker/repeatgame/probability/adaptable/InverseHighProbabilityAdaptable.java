package HideAndSeek.seeker.repeatgame.probability.adaptable;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.graph.GraphController;
import HideAndSeek.graph.StringEdge;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.AdaptiveSeeker;
import HideAndSeek.seeker.Seeker;
import HideAndSeek.seeker.repeatgame.probability.VariableNodesInverseHighProbability;
import Utility.AdaptiveUtils;
import Utility.Metric;
import Utility.Utils;

/**
 * Augmented to provide feedback on its own performance, such that
 * it may be changed by a parent strategy.
 * 
 * @author Martin
 *
 */
public class InverseHighProbabilityAdaptable extends VariableNodesInverseHighProbability implements AdaptiveSeeker {

	/**
	 * 
	 */
	private ArrayList<Double> percentageChanges;
	
	/**
	 * @param graphController
	 */
	public InverseHighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController, String name, int predictiveNodes) {
		
		super(graphController, name, predictiveNodes);
		
		percentageChanges = new ArrayList<Double>();
		
	}

	
	/**
	 * @param graphController
	 */
	public InverseHighProbabilityAdaptable(GraphController<StringVertex, StringEdge> graphController, int predictiveNodes) {
		
		this(graphController, "", predictiveNodes);
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.HighProbability#addHideLocation(HideAndSeek.graph.StringVertex)
	 */
	public void addHideLocation(StringVertex location) {
		
		super.addHideLocation(location);
		
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
	 * @see HideAndSeek.seeker.AdaptiveSeekerStrategy#performanceOfSelf()
	 */
	@Override
	public double performanceOfSelf() {

		percentageChanges.add(graphController.latestTraverserRoundPerformance(responsibleAgent, Metric.COST_CHANGE_SCORE));
		
		if (AdaptiveUtils.containsLowPerformance(percentageChanges)) {
			
			Utils.talk(toString(), "Consecutive low performance detected.");
			
			return 0.0;
			
		} else {
			
			return 1.0;
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.seeker.repeatgame.probability.InverseHighProbability#endOfGame()
	 */
	@Override
	public void endOfGame() {
		
		super.endOfGame();
		
	}
	
	/* (non-Javadoc)
	 * @see HideAndSeek.AdaptiveGraphTraverser#stopStrategy()
	 */
	@Override
	public void stopStrategy() {
		
		percentageChanges.clear();
		
	}
}
